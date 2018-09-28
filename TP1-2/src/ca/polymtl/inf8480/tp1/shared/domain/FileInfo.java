package ca.polymtl.inf8480.tp1.shared.domain;

import java.io.Serializable;
import java.util.Objects;

public class FileInfo implements Serializable {
    private String name;
    private String lockUser;

    public FileInfo(String name, String lockUser) {
        this.name = name;
        this.lockUser = lockUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLockUser() {
        return lockUser;
    }

    public void setLockUser(String lockUser) {
        this.lockUser = lockUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfo)) return false;
        FileInfo file = (FileInfo) o;
        return Objects.equals(getName(), file.getName()) &&
                Objects.equals(getLockUser(), file.getLockUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLockUser());
    }
}
