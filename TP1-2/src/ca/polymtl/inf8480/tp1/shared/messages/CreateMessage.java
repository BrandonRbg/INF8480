package ca.polymtl.inf8480.tp1.shared.messages;

import java.io.Serializable;
import java.util.Objects;

public class CreateMessage extends Message implements Serializable {
    private String name;

    public CreateMessage(String login, String password, String name) {
        super(login, password);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreateMessage that = (CreateMessage) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
