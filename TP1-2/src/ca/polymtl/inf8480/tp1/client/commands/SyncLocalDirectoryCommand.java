package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.client.Client;
import ca.polymtl.inf8480.tp1.client.utils.Config;
import ca.polymtl.inf8480.tp1.client.utils.ConfigManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.domain.FileDetails;
import ca.polymtl.inf8480.tp1.shared.messages.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SyncLocalDirectoryCommand extends Command {
    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {

            // Erase files
            File f = new File(Client.PATH);
            f.getParentFile().mkdirs();
            if (f.listFiles() != null) {
                for (File file : f.listFiles()) {
                    file.delete();
                }
            }

            // Sync files
            List<FileDetails> files = serverInterface.syncLocalDirectory(new Message(
                    ConfigManager.getConfig().getLogin(),
                    ConfigManager.getConfig().getPassword()
            ));
            for (FileDetails file : files) {
                FileOutputStream fileOutputStream = new FileOutputStream(Client.PATH + File.separator + file.getName());
                fileOutputStream.write(file.getContent());
                fileOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
