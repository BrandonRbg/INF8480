package ca.polymtl.inf8480.tp1.shared.messages;

import ca.polymtl.inf8480.tp1.shared.domain.LoadBalancerCredentials;
import ca.polymtl.inf8480.tp1.shared.domain.Operation;

import java.io.Serializable;

public class TaskMessage implements Serializable {
    private LoadBalancerCredentials credentials;
    private Operation[] operations;

    public TaskMessage(LoadBalancerCredentials credentials, Operation[] operations) {
        this.credentials = credentials;
        this.operations = operations;
    }

    public LoadBalancerCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(LoadBalancerCredentials credentials) {
        this.credentials = credentials;
    }

    public Operation[] getOperations() {
        return operations;
    }

    public void setOperations(Operation[] operations) {
        this.operations = operations;
    }
}
