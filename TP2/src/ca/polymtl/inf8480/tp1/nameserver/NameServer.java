package ca.polymtl.inf8480.tp1.nameserver;


import ca.polymtl.inf8480.tp1.shared.NameServerInterface;
import ca.polymtl.inf8480.tp1.shared.domain.CalculationServerInfo;
import ca.polymtl.inf8480.tp1.shared.domain.LoadBalancerCredentials;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NameServer implements NameServerInterface {
    public static void main(String[] args) {
        NameServer nameServer = new NameServer();
        nameServer.run();
    }

    private List<LoadBalancerCredentials> loadBalancerCredentials = Arrays.asList(
            new LoadBalancerCredentials("loadbalancer", "123456")
    );

    public void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            NameServerInterface stub = (NameServerInterface) UnicastRemoteObject
                    .exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("nameserver", stub);
            System.out.println("NameServer ready.");
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public boolean authorizeServer(LoadBalancerCredentials credentials) {
        return loadBalancerCredentials.contains(credentials);
    }

    @Override
    public List<CalculationServerInfo> getAllCalculationServers() {
        return null;
    }

    @Override
    public void registerServer(CalculationServerInfo serverInfo) {

    }
}
