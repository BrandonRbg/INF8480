package ca.polymtl.inf8480.tp2.shared.domain;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoadBalancerCredentials)) return false;
        LoadBalancerCredentials that = (LoadBalancerCredentials) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword());
    }
}
