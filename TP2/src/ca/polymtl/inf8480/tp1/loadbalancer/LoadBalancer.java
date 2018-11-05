package ca.polymtl.inf8480.tp1.loadbalancer;

import ca.polymtl.inf8480.tp1.shared.CalculationServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoadBalancer {
    public static void main(String[] args) {
        try {
            String operationsFilePath = args[0];
            boolean secureMode = Boolean.parseBoolean(args[1]);
            LoadBalancer loadBalancer = new LoadBalancer(operationsFilePath, secureMode);
            loadBalancer.run();
        } catch (Exception e) {
            System.out.println("Please provide every argument: ./loadbalancer.jar FILE_PATH SECURE_MODE");
        }
    }

    private static CalculationServerInterface loadCalculationServerStub(String hostname, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, port);
            return (CalculationServerInterface) registry.lookup("calculationserver");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
        return null;
    }

    private String operationsFilePath;
    private boolean secureMode;

    public LoadBalancer(String operationsFilePath, boolean secureMode) {
        this.operationsFilePath = operationsFilePath;
        this.secureMode = secureMode;
    }

    public void run() {

    }
}
