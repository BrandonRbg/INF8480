package ca.polymtl.inf8480.tp1.calculationserver;

import ca.polymtl.inf8480.tp1.calculationserver.config.ArgumentsParser;
import ca.polymtl.inf8480.tp1.calculationserver.config.Config;
import ca.polymtl.inf8480.tp1.calculationserver.operations.OperationsService;
import ca.polymtl.inf8480.tp1.shared.CalculationServerInterface;
import ca.polymtl.inf8480.tp1.shared.messages.TaskMessage;
import ca.polymtl.inf8480.tp1.shared.responses.TaskResponse;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.stream.Stream;

public class CalculationsServer implements CalculationServerInterface {

    public static void main(String[] args) {
        Config config = ArgumentsParser.parseArguments(args);
        if (config != null) {
            CalculationsServer server = new CalculationsServer(config);
            server.run();
        } else {
            System.out.println("Please provide every argument: ./calculationserver.jar MAX_OPERATIONS WRONG_ANSWER_RATIO HOSTNAME PORT");
        }
    }

    private Config config;
    private OperationsService operationsService;

    public CalculationsServer(Config config) {
        super();
        this.config = config;
        this.operationsService = new OperationsService(config.getWrongAnswersRatio());
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // TODO Register server to nameserver

        try {
            CalculationServerInterface stub = (CalculationServerInterface) UnicastRemoteObject
                    .exportObject(this, config.getPort());

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("calculationserver", stub);
            System.out.println("CalculationsServer ready.");
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
        // TODO  Authenticate server

        int[] result = Stream.of(taskMessage.getOperations())
                .map(o -> operationsService.executeOperation(o))
                .mapToInt(i -> i).toArray();

        return new TaskResponse(result);
    }
}

