package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.client.Client;
import ca.polymtl.inf8480.tp1.client.utils.ConfigManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.messages.NameContentMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PushCommand extends Command {

    private String name;

    public PushCommand(String name) {
        this.name = name;
    }

    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {
            File f = new File(Client.PATH + File.separator + name);
            byte[] fileContent = Files.readAllBytes(f.toPath());

            serverInterface.push(
                    new NameContentMessage(
                            ConfigManager.getConfig().getLogin(),
                            ConfigManager.getConfig().getPassword(),
                            name,
                            fileContent
                    )
            );

            return name + " a été envoyé au serveur";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
