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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LoadBalancer {
    public static void main(String[] args) {
        try {
            Config config = ArgumentsParser.parseArguments(args);
            LoadBalancer loadBalancer = new LoadBalancer(config);
            loadBalancer.run();
        } catch (Exception e) {
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
            System.out.println("Erreur: " + e.getMessage());
        }
        return null;
    }

    private List<CalculationServerInterface> calculationServers;
    private List<CalculationServerInfo> calculationServerInfos;
    private NameServerInterface nameServerStub;
    private Config config;
    private LoadBalancerCredentials credentials;
    private int nextServer = 0;

    private List<CompletableFuture<List<Integer>>> runningOperations = new ArrayList<>();

    public LoadBalancer(Config config) {
        this.config = config;
        this.nameServerStub = loadNameServerStub(config.getHostname());
        this.credentials = new LoadBalancerCredentials(config.getUsername(), config.getPassword());
        try {
            this.calculationServerInfos = nameServerStub.getAllCalculationServers();
            this.calculationServers = getCalculationServers();
        } catch (RemoteException e) {
            System.out.println("Failed to connect to nameserver at " + config.getHostname() + ".");
        }
    }

    public void run() {
        try {
            List<Operation> allOperations = getOperations();
            int chunkSize = config.getChunkSize();
            while (!allOperations.isEmpty()) {
                List<Operation> operationsToProcess = allOperations.size() < chunkSize ? allOperations : allOperations.subList(0, chunkSize);
                runningOperations.add(startCalculationsOnBatch(operationsToProcess));
                allOperations.removeAll(operationsToProcess);
            }

            int result = runningOperations.stream()
                    .map(CompletableFuture::join)
                    .flatMap(Collection::stream)
                    .mapToInt(Integer::intValue)
                    .sum();

            System.out.println("The result is " + result);

        } catch (IOException e) {
            System.out.println("Failed to parse operations.");
        }
    }

    private CompletableFuture<List<Integer>> startCalculationsOnBatch(List<Operation> batch) {
        return CompletableFuture.supplyAsync(() -> getResult(batch));
    }

    private List<String> getOperationsStrings() throws IOException {
        return Files.readAllLines(Paths.get(config.getOperationsFilePath()));
    }

    private List<Operation> getOperations() throws IOException {
        return getOperationsStrings().parallelStream()
                .map(Operation::parseOperation)
                .collect(Collectors.toList());
    }

    private List<Integer> getResult(List<Operation> batch) {
        try {
            if (config.isSecureMode()) {
                return getResponse(batch).getResult();
            } else {
                return getResponseSecure(batch).getResult();
            }
        } catch (RemoteException e) {
            return getResult(batch);
        }
    }

    private TaskResponse getResponseSecure(List<Operation> batch) throws RemoteException {
        List<TaskResponse> responses = new ArrayList<>();
        TaskResponse lastResponse = getResponse(batch);
        while (!responses.contains(lastResponse)) {
            responses.add(lastResponse);
            lastResponse = getResponse(batch);
        }
        return lastResponse;
    }

    private TaskResponse getResponse(List<Operation> batch) throws RemoteException {
        try {
            return getNextCalculationServer().execute(new TaskMessage(credentials, batch));
        } catch (RemoteException e) {
            System.out.println("Error while sending task to server #" + nextServer);
            throw e;
        }
    }

    private CalculationServerInterface getNextCalculationServer() {
        return calculationServers.get((nextServer++) % calculationServers.size());
    }

    private List<CalculationServerInterface> getCalculationServers() {
        return calculationServerInfos.stream()
                .map(c -> loadCalculationServerStub(c.getHostname(), c.getPort()))
                .collect(Collectors.toList());
    }
}
