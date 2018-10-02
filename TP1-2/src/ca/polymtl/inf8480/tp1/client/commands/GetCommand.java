package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.client.Client;
import ca.polymtl.inf8480.tp1.client.utils.ConfigManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.Utils;
import ca.polymtl.inf8480.tp1.shared.domain.FileDetails;
import ca.polymtl.inf8480.tp1.shared.messages.NameChecksumMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class GetCommand extends Command {

    private String name;

    public GetCommand(String name) {
        this.name = name;
    }

    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {
            File f = new File(Client.PATH + File.separator + name);
            f.getParentFile().mkdirs();
            byte[] checksum = null;
            if (f.exists()) {
                byte[] fileContent = Files.readAllBytes(f.toPath());
                checksum = Utils.getMD5(fileContent);
            }
            FileDetails newFileDetails = serverInterface.get(
                    new NameChecksumMessage(
                            ConfigManager.getConfig().getLogin(),
                            ConfigManager.getConfig().getPassword(),
                            name,
                            checksum
                    )
            );

            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(f);
            fileOutputStream.write(newFileDetails.getContent());
            fileOutputStream.close();

            return name + " synchronisé";
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
