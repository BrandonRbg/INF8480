package ca.polymtl.inf8480.tp2.calculationsserver;

import ca.polymtl.inf8480.tp2.calculationsserver.config.ArgumentsParser;
import ca.polymtl.inf8480.tp2.calculationsserver.config.Config;
import ca.polymtl.inf8480.tp2.calculationsserver.operations.OperationsService;
import ca.polymtl.inf8480.tp2.calculationsserver.operations.ResourcesService;
import ca.polymtl.inf8480.tp2.shared.CalculationServerInterface;
import ca.polymtl.inf8480.tp2.shared.NameServerInterface;
import ca.polymtl.inf8480.tp2.shared.domain.CalculationServerInfo;
import ca.polymtl.inf8480.tp2.shared.messages.TaskMessage;
import ca.polymtl.inf8480.tp2.shared.responses.TaskResponse;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.stream.Collectors;

public class CalculationsServer implements CalculationServerInterface {

    private static int total = 0;

    public static void main(String[] args) {
        Config config = ArgumentsParser.parseArguments(args);
        CalculationsServer server = new CalculationsServer(config);
        server.run();
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

    private Config config;
    private OperationsService operationsService;
    private NameServerInterface nameServerStub;

    public CalculationsServer(Config config) {
        super();
        this.config = config;
        this.operationsService = new OperationsService(config.getWrongAnswersRatio());
        this.nameServerStub = loadNameServerStub(this.config.getNameserverHostname());
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            CalculationServerInterface stub = (CalculationServerInterface) UnicastRemoteObject
                    .exportObject(this, config.getPort());

            Registry registry = LocateRegistry.createRegistry(config.getPort());
            registry.rebind("calculationsserver", stub);
            System.out.println("CalculationsServer ready.");

            nameServerStub.registerServer(new CalculationServerInfo(config.getHostname(), config.getPort(), config.getMaxOperationsPerRequest()));

        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }


    @Override
    public TaskResponse execute(TaskMessage taskMessage) throws RemoteException {
        System.out.println("Started at " + System.currentTimeMillis());
        if (!nameServerStub.authorizeServer(taskMessage.getCredentials())) {
            throw new RuntimeException("Unauthorized server");
        }

        if (shouldRefuseTask(ResourcesService.getRefusalRatio(taskMessage.getOperations().size(), config.getMaxOperationsPerRequest()))) {
            throw new RuntimeException("Too much load");
        }
        total += taskMessage.getOperations().size();
        int result = taskMessage.getOperations().parallelStream()
                .map(o -> operationsService.executeOperation(o))
                .mapToInt(Integer::intValue)
                .reduce(0, (acc, i) -> (acc + i) % 4000);
        System.out.println("Ended at " + System.currentTimeMillis());
        return new TaskResponse(result);
    }

    private boolean shouldRefuseTask(float refusalRatio) {
        return Math.random() < refusalRatio;
    }
}

