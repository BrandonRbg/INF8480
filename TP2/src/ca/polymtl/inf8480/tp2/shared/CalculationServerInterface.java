package ca.polymtl.inf8480.tp2.shared;

import ca.polymtl.inf8480.tp2.shared.messages.TaskMessage;
import ca.polymtl.inf8480.tp2.shared.responses.TaskResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalculationServerInterface extends Remote {

	TaskResponse execute(TaskMessage taskMessage) throws RemoteException;
}
