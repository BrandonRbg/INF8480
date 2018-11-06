package ca.polymtl.inf8480.tp2.shared;

import ca.polymtl.inf8480.tp2.shared.domain.CalculationServerInfo;
import ca.polymtl.inf8480.tp2.shared.domain.LoadBalancerCredentials;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NameServerInterface extends Remote {
    boolean authorizeServer(LoadBalancerCredentials credentials) throws RemoteException;

    List<CalculationServerInfo> getAllCalculationServers() throws RemoteException;

    void registerServer(CalculationServerInfo serverInfo) throws RemoteException;
}
