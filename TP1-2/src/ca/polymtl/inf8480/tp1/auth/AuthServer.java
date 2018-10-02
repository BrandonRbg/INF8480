package ca.polymtl.inf8480.tp1.auth;

import ca.polymtl.inf8480.tp1.auth.utils.AuthenticationManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.messages.Message;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class AuthServer implements AuthServerInterface {

    public static void main(String[] args) {
        AuthServer server = new AuthServer();
        server.run();
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            AuthServerInterface stub = (AuthServerInterface) UnicastRemoteObject
                    .exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("auth", stub);
            System.out.println("Auth server ready.");
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void createUser(Message message) throws RuntimeException, IOException {
        AuthenticationManager.createUser(message.getLogin(), message.getPassword());
    }

    @Override
    public boolean verify(Message message) throws RuntimeException {
        return AuthenticationManager.verifyUser(message.getLogin(), message.getPassword());
    }
}
