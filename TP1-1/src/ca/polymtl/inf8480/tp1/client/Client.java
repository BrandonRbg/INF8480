package ca.polymtl.inf8480.tp1.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ca.polymtl.inf8480.tp1.shared.ServerInterface;

public class Client {
    private static final int BYTE_MARTIN = 100;

    public static void main(String[] args) throws InterruptedException {
        String distantHostname = null;

        if (args.length > 0) {
            distantHostname = args[0];
        }

        Client client = new Client(distantHostname);
        client.run();
    }

    FakeServer localServer = null; // Pour tester la latence d'un appel de
    // fonction normal.
    private ServerInterface localServerStub = null;
    private ServerInterface distantServerStub = null;

    public Client(String distantServerHostname) {
        super();

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        localServer = new FakeServer();
        localServerStub = loadServerStub("127.0.0.1");

        if (distantServerHostname != null) {
            distantServerStub = loadServerStub(distantServerHostname);
        }
    }

    private void run() throws InterruptedException {
        for (int i = 1; i <= 7; ++i) {
            System.out.println("~~~~~~~~~~~~~~~~~~~10^" + i + "~~~~~~~~~~~~~~~~~~~");
            for (int j = 0; j < 2; ++j) {
                appelNormal(i);

                if (localServerStub != null) {
                    appelRMILocal(i);
                }

                if (distantServerStub != null) {
                    appelRMIDistant(i);
                }
            }

            Thread.sleep(2000);
        }
    }

    private ServerInterface loadServerStub(String hostname) {
        ServerInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            stub = (ServerInterface) registry.lookup("server");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (AccessException e) {
            System.out.println("Erreur: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        return stub;
    }

    private void appelNormal(int power) {
        long start = System.nanoTime();
        localServer.execute(new byte[(int) Math.pow(10, power)]);
        long end = System.nanoTime();

        System.out.println("Temps écoulé appel normal: " + (end - start) + " ns");
        System.out.println("Résultat appel normal: SUCCESS");
    }

    private void appelRMILocal(int power) {
        try {
            long start = System.nanoTime();
            localServerStub.execute(new byte[(int) Math.pow(10, power)]);
            long end = System.nanoTime();

            System.out.println("Temps écoulé appel RMI local: " + (end - start) + " ns");
            System.out.println("Résultat appel RMI local: SUCCESS");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void appelRMIDistant(int power) {
        try {
            long start = System.nanoTime();
            distantServerStub.execute(new byte[(int) Math.pow(10, power)]);
            long end = System.nanoTime();

            System.out.println("Temps écoulé appel RMI distant: " + (end - start) + " ns");
            System.out.println("Résultat appel RMI distant: SUCCESS");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}
