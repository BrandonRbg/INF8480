package ca.polymtl.inf8480.tp1.loadbalancer.config;

public class Config {
    private String operationsFilePath = "./operations-588";
    private boolean secureMode = false;
    private String username = "loadbalancer";
    private String password = "123456";

    public Config() {

    }

    public Config(String operationsFilePath, boolean secureMode, String username, String password) {
        this.operationsFilePath = operationsFilePath;
        this.secureMode = secureMode;
        this.username = username;
        this.password = password;
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
}
