package ca.polymtl.inf8480.tp1.client.commands;

import ca.polymtl.inf8480.tp1.client.utils.Config;
import ca.polymtl.inf8480.tp1.client.utils.ConfigManager;
import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.domain.FileInfo;
import ca.polymtl.inf8480.tp1.shared.messages.Message;

import java.rmi.RemoteException;
import java.util.List;

public class ListCommand extends Command {

    @Override
    public String execute(ServerInterface serverInterface, AuthServerInterface authServerInterface) {
        try {
            StringBuilder returnValue = new StringBuilder();
            List<FileInfo> fileInfos = serverInterface.list(new Message(
                    ConfigManager.getConfig().getLogin(),
                    ConfigManager.getConfig().getPassword()
            ));
            for (FileInfo fileInfo : fileInfos) {
                String lockUser = fileInfo.getLockUser() != null ? "verrouillé par " + fileInfo.getLockUser() : "non verrouillé";
                returnValue.append("* " + fileInfo.getName() + "\t" + lockUser + "\n");
            }
            return returnValue.toString() + fileInfos.size() + " fichier (s)";
        } catch (RuntimeException | RemoteException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
