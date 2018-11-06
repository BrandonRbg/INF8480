package ca.polymtl.inf8480.tp2.shared.messages;

import ca.polymtl.inf8480.tp2.shared.domain.LoadBalancerCredentials;
import ca.polymtl.inf8480.tp2.shared.domain.Operation;

import java.io.Serializable;
import java.util.List;

public class TaskMessage implements Serializable {
    private LoadBalancerCredentials credentials;
    private List<Operation> operations;

    public TaskMessage(LoadBalancerCredentials credentials, List<Operation> operations) {
        this.credentials = credentials;
        this.operations = operations;
    }

    public LoadBalancerCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(LoadBalancerCredentials credentials) {
        this.credentials = credentials;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
