package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.messages.Message;

import java.io.IOException;

public class CreateUserCommand extends Command {

    private String login;
    private String password;

    public CreateUserCommand(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {
            authServerInterface.createUser(new Message(login, password));
        } catch (IOException e) {
            return "Erreur lors de la création du compte.";
        }
        return "Compte créé avec succès.";
    }

    @Override
    public boolean requiresAuthentication() {
        return false;
    }
}
