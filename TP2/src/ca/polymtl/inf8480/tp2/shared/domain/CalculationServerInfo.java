package ca.polymtl.inf8480.tp2.shared.domain;

import java.io.Serializable;

public class CalculationServerInfo implements Serializable {
    private String hostname;
    private int port;
    private int maxOperationsPerRequest;

    public CalculationServerInfo(String hostname, int port, int maxOperationsPerRequest) {
        this.hostname = hostname;
        this.port = port;
        this.maxOperationsPerRequest = maxOperationsPerRequest;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxOperationsPerRequest() {
        return maxOperationsPerRequest;
    }

    public void setMaxOperationsPerRequest(int maxOperationsPerRequest) {
        this.maxOperationsPerRequest = maxOperationsPerRequest;
    }
}
