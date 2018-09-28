package ca.polymtl.inf8480.tp1.shared.messages;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class NameContentMessage extends Message implements Serializable {
    private String name;
    private byte[] content;

    public NameContentMessage(String login, String password, String name, byte[] content) {
        super(login, password);
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameContentMessage)) return false;
        NameContentMessage that = (NameContentMessage) o;
        return Objects.equals(getName(), that.getName()) &&
                Arrays.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContent());
    }
}
