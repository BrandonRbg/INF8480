package ca.polymtl.inf8480.tp1.shared;

import ca.polymtl.inf8480.tp1.shared.domain.FileDetails;
import ca.polymtl.inf8480.tp1.shared.domain.FileInfo;
import ca.polymtl.inf8480.tp1.shared.messages.CreateMessage;
import ca.polymtl.inf8480.tp1.shared.messages.Message;
import ca.polymtl.inf8480.tp1.shared.messages.NameChecksumMessage;
import ca.polymtl.inf8480.tp1.shared.messages.NameContentMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote {
    void create(CreateMessage createMessage) throws RuntimeException, RemoteException;

    List<FileInfo> list(Message message) throws RemoteException;

    List<FileDetails> syncLocalDirectory(Message message) throws RemoteException;

    FileDetails get(NameChecksumMessage nameChecksumMessage) throws RuntimeException, RemoteException;

    FileDetails lock(NameChecksumMessage nameChecksumMessage) throws RuntimeException, RemoteException;

    void push(NameContentMessage nameContentMessage) throws RuntimeException, RemoteException;
}
