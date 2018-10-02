package ca.polymtl.inf8480.tp1.auth.utils;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationManager {
    private static final String FILE_NAME = "USERS";
    private static ConcurrentHashMap<String, String> users;

    static {
        try {
            users = getFromFile();
        } catch (IOException | ClassNotFoundException e) {
            users = new ConcurrentHashMap<>();
        }
    }

    public static void createUser(String login, String password) throws IOException {
        if (users.containsKey(login)) {
            throw new RuntimeException("Un utilisateur existe déjà avec ce nom.");
        }
        users.put(login, password);
        saveToFile();
    }

    public static boolean verifyUser(String login, String password) {
        return users.containsKey(login) && users.get(login).equals(password);
    }

    private static void saveToFile() throws IOException {
        File outputFile = new File(FILE_NAME);
        ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(outputFile));
        objectOut.writeObject(users);
    }

    private static ConcurrentHashMap<String, String> getFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(FILE_NAME));
        return (ConcurrentHashMap<String, String>) objectIn.readObject();
    }
}
