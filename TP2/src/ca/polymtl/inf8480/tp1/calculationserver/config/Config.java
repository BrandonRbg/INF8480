package ca.polymtl.inf8480.tp1.calculationserver.config;

public class Config {
    private int maxOperationsPerRequest = 4;
    private float wrongAnswersRatio = 0;
    private String hostname = "127.0.0.1";
    private int port = 5000;

    public Config(int maxOperationsPerRequest, float wrongAnswersRatio, String hostname, int port) {
        this.maxOperationsPerRequest = maxOperationsPerRequest;
        this.wrongAnswersRatio = wrongAnswersRatio;
        this.hostname = hostname;
        this.port = port;
    }

    public int getMaxOperationsPerRequest() {
        return maxOperationsPerRequest;
    }

    public void setMaxOperationsPerRequest(int maxOperationsPerRequest) {
        this.maxOperationsPerRequest = maxOperationsPerRequest;
    }

    public float getWrongAnswersRatio() {
        return wrongAnswersRatio;
    }

    public void setWrongAnswersRatio(float wrongAnswersRatio) {
        this.wrongAnswersRatio = wrongAnswersRatio;
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
}
