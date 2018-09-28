package ca.polymtl.inf8480.tp1.client;

import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.Utils;
import ca.polymtl.inf8480.tp1.shared.domain.FileDetails;
import ca.polymtl.inf8480.tp1.shared.messages.NameChecksumMessage;
import ca.polymtl.inf8480.tp1.shared.messages.NameContentMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class Client {
    private static final String PATH = "/tmp/martin";

    public static void main(String[] args) throws InterruptedException {
        String distantHostname = null;

        if (args.length > 0) {
            distantHostname = args[0];
        }

        Client client = new Client(distantHostname);
    }

    private ServerInterface distantServerStub = null;

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

    public Client(String distantServerHostname) {
        super();

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        distantServerStub = loadServerStub(distantServerHostname);

        File file = new File(PATH + File.separator + "test.txt");
        try {
            distantServerStub.push(new NameContentMessage("", "", "test.txt", "TEST TEST TEST".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
