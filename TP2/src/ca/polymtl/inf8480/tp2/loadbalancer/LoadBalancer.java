package ca.polymtl.inf8480.tp2.loadbalancer;

import ca.polymtl.inf8480.tp2.loadbalancer.config.ArgumentsParser;
import ca.polymtl.inf8480.tp2.loadbalancer.config.Config;
import ca.polymtl.inf8480.tp2.shared.CalculationServerInterface;
import ca.polymtl.inf8480.tp2.shared.NameServerInterface;
import ca.polymtl.inf8480.tp2.shared.domain.CalculationServerInfo;
import ca.polymtl.inf8480.tp2.shared.domain.LoadBalancerCredentials;
import ca.polymtl.inf8480.tp2.shared.domain.Operation;
import ca.polymtl.inf8480.tp2.shared.messages.TaskMessage;
import ca.polymtl.inf8480.tp2.shared.responses.TaskResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class LoadBalancer {
    public static void main(String[] args) {
        try {
            Config config = ArgumentsParser.parseArguments(args);
            LoadBalancer loadBalancer = new LoadBalancer(config);
            loadBalancer.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Please provide every argument: ./loadbalancer.jar FILE_PATH SECURE_MODE HOSTNAME USERNAME PASSWORD CHUNK_SIZE");
        }
    }

    private static NameServerInterface loadNameServerStub(String hostname) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            return (NameServerInterface) registry.lookup("nameserver");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return null;
    }

    private static CalculationServerInterface loadCalculationServerStub(String hostname, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, port);
            return (CalculationServerInterface) registry.lookup("calculationsserver");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (RemoteException e) {
            System.out.println("Erreur: loadCalculationsServerStub " + e.getMessage());
        }
        return null;
    }

    private List<CalculationServerInterface> calculationServers;
    private List<CalculationServerInfo> calculationServerInfos;
    private NameServerInterface nameServerStub;
    private Config config;
    private LoadBalancerCredentials credentials;
    private int nextServer = 0;

    private ConcurrentLinkedDeque<List<Operation>> chunks = new ConcurrentLinkedDeque<>();

    public LoadBalancer(Config config) {
        this.config = config;
        this.nameServerStub = loadNameServerStub(config.getHostname());
        this.credentials = new LoadBalancerCredentials(config.getUsername(), config.getPassword());
        try {
            this.calculationServerInfos = nameServerStub.getAllCalculationServers();
            this.calculationServers = getCalculationServers();
            System.out.println("Started loadbalancer with " + this.calculationServerInfos.size() + " servers.");
        } catch (RemoteException e) {
            System.out.println("Failed to connect to nameserver at " + config.getHostname() + ".");
        }
    }

    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            List<Operation> allOperations = getOperations();
            int chunkSize = config.getChunkSize();
            while (!allOperations.isEmpty()) {
                List<Operation> operationsToProcess = new ArrayList<>(allOperations.size() < chunkSize ? allOperations : allOperations.subList(0, chunkSize));
                chunks.add(operationsToProcess);
                allOperations.removeAll(operationsToProcess);
            }
            List<Future<Integer>> futures = new ArrayList<>();
            for (int server = 0; server < calculationServers.size(); ++server) {
                futures.add(startCalculationsOnNextChunk(chunks, server, 0));
            }
            int result = 0;
            for (Future<Integer> future : futures) {
                result += future.get();
            }

            result %= 4000;

            long delta = System.currentTimeMillis() - startTime;

            System.out.println("The result is " + result + ", it took " + delta + "ms");

        } catch (IOException e) {
            System.out.println("Failed to parse operations.");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Task failed");
        }
    }

    private CompletableFuture<Integer> startCalculationsOnNextChunk(Deque<List<Operation>> chunks, int server, int lastResult) {
        if (chunks.isEmpty()) {
            return CompletableFuture.supplyAsync(() -> lastResult);
        }
        List<Operation> nextChunk = chunks.pop();
        return startCalculationsOnBatch(nextChunk, server)
                .thenCompose(result -> startCalculationsOnNextChunk(chunks, server, (lastResult + result) % 4000));
    }

    private CompletableFuture<Integer> startCalculationsOnBatch(List<Operation> batch, int server) {
        return CompletableFuture.supplyAsync(() -> getResult(batch, server), Executors.newFixedThreadPool(calculationServerInfos.size()));
    }

    private List<String> getOperationsStrings() throws IOException {
        return Files.readAllLines(Paths.get(config.getOperationsFilePath()));
    }

    private List<Operation> getOperations() throws IOException {
        return getOperationsStrings().parallelStream()
                .map(Operation::parseOperation)
                .collect(Collectors.toList());
    }

    private int getResult(List<Operation> batch, int server) {
        try {
            if (config.isSecureMode()) {
                return getResponse(batch, server).getResult();
            } else {
                return getResponseSecure(batch, server).getResult();
            }
        } catch (Exception e) {
            return getResult(batch, getNextCalculationServer(server));
        }
    }

    private TaskResponse getResponseSecure(List<Operation> batch, int server) throws RemoteException {
        List<TaskResponse> responses = new ArrayList<>();
        TaskResponse lastResponse = getResponse(batch, server);
        int nextServer = getNextCalculationServer(server);
        while (!responses.contains(lastResponse)) {
            responses.add(lastResponse);
            lastResponse = getResponse(batch, nextServer);
            nextServer = getNextCalculationServer(nextServer);
        }
        return lastResponse;
    }

    private TaskResponse getResponse(List<Operation> batch, int server) throws RemoteException {
        try {
            return calculationServers.get(server).execute(new TaskMessage(credentials, batch));
        } catch (RemoteException e) {
            System.out.println("Error while sending task to server.");
            throw e;
        } catch (RuntimeException e) {
            System.out.println("Server has too much load.");
            throw e;
        }
    }

    private int getNextCalculationServer(int current) {
        return (current + 1) % calculationServers.size();
    }

    private List<CalculationServerInterface> getCalculationServers() {
        return calculationServerInfos.stream()
                .map(c -> loadCalculationServerStub(c.getHostname(), c.getPort()))
                .collect(Collectors.toList());
    }
}
