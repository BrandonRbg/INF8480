package ca.polymtl.inf8480.tp1.client;

import ca.polymtl.inf8480.tp1.client.commands.Command;
import ca.polymtl.inf8480.tp1.client.utils.CommandParser;
import ca.polymtl.inf8480.tp1.client.utils.ConfigManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static final String PATH = "/tmp/jacques";

    public static void main(String[] args) {
        try {
            Command command = CommandParser.parseArguments(args);
            if (command.requiresAuthentication() && ConfigManager.getConfig() == null
                    || ConfigManager.getConfig().getLogin() == null
                    || ConfigManager.getConfig().getPassword() == null
                    || ConfigManager.getConfig().getServerHostname() == null) {
                throw new RuntimeException("Veuillez vous authentifier avant d'effectuer une commande.");
            }
            System.out.println(command.execute(
                    Client.loadServerStub(ConfigManager.getConfig().getServerHostname()),
                    Client.loadAuthServerStub(ConfigManager.getConfig().getServerHostname())
            ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static ServerInterface loadServerStub(String hostname) {
        ServerInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            stub = (ServerInterface) registry.lookup("server");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        return stub;
    }

    private static AuthServerInterface loadAuthServerStub(String hostname) {
        AuthServerInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            stub = (AuthServerInterface) registry.lookup("auth");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        return stub;
    }
}
