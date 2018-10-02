package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.messages.Message;

import java.rmi.RemoteException;

public class LoginCommand extends Command {

    private String login;
    private String password;
    private String hostname;

    public LoginCommand(String login, String password, String hostname) {
        this.login = login;
        this.password = password;
        this.hostname = hostname;
    }

    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {
            if (!authServerInterface.verify(new Message(login, password))) {
                return "Erreur lors de l'authentification.";
            }
        } catch (RemoteException e) {
            return "Erreur lors de l'authentification.";
        }
        return "Authentification réussie.";
    }

    @Override
    public boolean requiresAuthentication() {
        return false;
    }
}
