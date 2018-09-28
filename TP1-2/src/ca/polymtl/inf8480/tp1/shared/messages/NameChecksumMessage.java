package ca.polymtl.inf8480.tp1.shared.messages;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class NameChecksumMessage extends Message implements Serializable {
    private String name;
    private byte[] checksum;

    public NameChecksumMessage(String login, String password, String name, byte[] checksum) {
        super(login, password);
        this.name = name;
        this.checksum = checksum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getChecksum() {
        return checksum;
    }

    public void setChecksum(byte[] checksum) {
        this.checksum = checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameChecksumMessage)) return false;
        if (!super.equals(o)) return false;
        NameChecksumMessage that = (NameChecksumMessage) o;
        return Objects.equals(getName(), that.getName()) &&
                Arrays.equals(getChecksum(), that.getChecksum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getChecksum());
    }
}
