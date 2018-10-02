package ca.polymtl.inf8480.tp1.client.utils;

import ca.polymtl.inf8480.tp1.client.commands.*;

import java.io.IOException;

public class CommandParser {
    public static Command parseArguments(String[] args) {
        switch (args[0]) {
            case "login":
                try {
                    String serverHostname = args[1];
                    String login = args[2];
                    String password = args[3];
                    ConfigManager.setConfig(new Config(login, password, serverHostname));
                    Config.saveToFile(ConfigManager.getConfig());
                    return new LoginCommand(login, password, serverHostname);
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Vous devez donner des paramètres:\n client login SERVER_HOSTNAME LOGIN PASSWORD");
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            case "user":
                try {
                    String login = args[1];
                    String password = args[2];
                    return new CreateUserCommand(login, password);
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Vous devez donner des paramètres:\n client user LOGIN PASSWORD");
                }
            case "create":
                try {
                    String filename = args[1];
                    return new CreateCommand(filename);
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Vous devez donner des paramètres:\n client create FILE_NAME");
                }
            case "get":
                try {
                    String filename = args[1];
                    return new GetCommand(filename);
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Vous devez donner des paramètres:\n client get FILE_NAME");
                }
            case "list":
                return new ListCommand();
            case "lock":
                try {
                    String filename = args[1];
                    return new LockCommand(filename);
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Vous devez donner des paramètres:\n client lock FILE_NAME");
                }
            case "push":
                try {
                    String filename = args[1];
                    return new PushCommand(filename);
                } catch (IndexOutOfBoundsException e) {
                    throw new RuntimeException("Vous devez donner des paramètres:\n client push FILE_NAME");
                }
            case "syncLocalDirectory":
                return new SyncLocalDirectoryCommand();
            default:
                throw new RuntimeException("Veuillez entrer une commande valide.");
        }
    }
}
