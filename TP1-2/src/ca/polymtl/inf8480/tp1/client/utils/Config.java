package ca.polymtl.inf8480.tp1.client.utils;

import java.io.*;

public class Config implements Serializable {
    private static final String FILE_NAME = "CONFIG";

    private String login;
    private String password;
    private String serverHostname;

    public Config(String login, String password, String serverHostname) {
        this.login = login;
        this.password = password;
        this.serverHostname = serverHostname;
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

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public static void saveToFile(Config credentials) throws IOException {
        File outputFile = new File(FILE_NAME);
        ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(outputFile));
        objectOut.writeObject(credentials);
    }

    public static Config getFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(FILE_NAME));
        return (Config) objectIn.readObject();
    }
}
