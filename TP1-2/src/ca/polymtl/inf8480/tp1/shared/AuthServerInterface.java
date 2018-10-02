package ca.polymtl.inf8480.tp1.shared;

import ca.polymtl.inf8480.tp1.shared.messages.Message;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthServerInterface extends Remote {
    void createUser(Message message) throws RuntimeException, RemoteException, IOException;
    boolean verify(Message message) throws RuntimeException, RemoteException;
}
