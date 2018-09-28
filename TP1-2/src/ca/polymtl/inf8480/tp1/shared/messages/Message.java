package ca.polymtl.inf8480.tp1.shared.messages;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {
    private String login;
    private String password;

    public Message(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(login, message.login) &&
                Objects.equals(password, message.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
