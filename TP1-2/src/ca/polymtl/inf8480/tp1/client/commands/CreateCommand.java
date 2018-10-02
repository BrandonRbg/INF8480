package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.client.utils.ConfigManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.messages.CreateMessage;

import java.rmi.RemoteException;

public class CreateCommand extends Command {
    private String filename;

    public CreateCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {
            serverInterface.create(new CreateMessage(
                    ConfigManager.getConfig().getLogin(),
                    ConfigManager.getConfig().getPassword(),
                    filename)
            );
            return filename + " ajouté.";
        } catch (RuntimeException | RemoteException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
