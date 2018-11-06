package ca.polymtl.inf8480.tp1.loadbalancer;

import ca.polymtl.inf8480.tp1.loadbalancer.config.ArgumentsParser;
import ca.polymtl.inf8480.tp1.loadbalancer.config.Config;
import ca.polymtl.inf8480.tp1.shared.CalculationServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoadBalancer {
    public static void main(String[] args) {
        try {
            Config config = ArgumentsParser.parseArguments(args);
            LoadBalancer loadBalancer = new LoadBalancer(config);
            loadBalancer.run();
        } catch (Exception e) {
            System.out.println("Please provide every argument: ./loadbalancer.jar FILE_PATH SECURE_MODE USERNAME PASSWORD");
        }
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

    private CalculationServerInterface calculationServerStub;
    private Config config;

    public LoadBalancer(Config config) {
        this.config = config;
    }

    public void run() {

    }
}
