package ca.polymtl.inf8480.tp1.shared.domain;

import java.io.Serializable;

public class LoadBalancerCredentials implements Serializable {
    private String username;
    private String password;

    public LoadBalancerCredentials(String username, String password) {
        this.username = username;
        this.password = password;
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
