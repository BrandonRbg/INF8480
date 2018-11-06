package ca.polymtl.inf8480.tp2.loadbalancer.config;

public class Config {
    private String operationsFilePath = "./operations-588";
    private boolean secureMode = false;
    private String hostname = "127.0.0.1";
    private String username = "loadbalancer";
    private String password = "123456";
    private int chunkSize = 5;

    public Config() {

    }

    public Config(String operationsFilePath, boolean secureMode, String hostname, String username, String password, int chunkSize) {
        this.operationsFilePath = operationsFilePath;
        this.secureMode = secureMode;
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.chunkSize = chunkSize;
    }

    public String getOperationsFilePath() {
        return operationsFilePath;
    }

    public void setOperationsFilePath(String operationsFilePath) {
        this.operationsFilePath = operationsFilePath;
    }

    public boolean isSecureMode() {
        return secureMode;
    }

    public void setSecureMode(boolean secureMode) {
        this.secureMode = secureMode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
