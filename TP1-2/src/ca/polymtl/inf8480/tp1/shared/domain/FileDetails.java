package ca.polymtl.inf8480.tp1.shared.domain;

public class FileDetails extends FileInfo {
    private byte[] content;

    public FileDetails(String name, String lockUser, byte[] content) {
        super(name, lockUser);
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
