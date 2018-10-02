package ca.polymtl.inf8480.tp1.server;

import ca.polymtl.inf8480.tp1.shared.AuthServerInterface;
import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.Utils;
import ca.polymtl.inf8480.tp1.shared.domain.FileDetails;
import ca.polymtl.inf8480.tp1.shared.domain.FileInfo;
import ca.polymtl.inf8480.tp1.shared.messages.CreateMessage;
import ca.polymtl.inf8480.tp1.shared.messages.Message;
import ca.polymtl.inf8480.tp1.shared.messages.NameChecksumMessage;
import ca.polymtl.inf8480.tp1.shared.messages.NameContentMessage;

import java.io.*;
import java.nio.file.Files;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Server implements ServerInterface {

    private static final String FILE_NAME = "LOCKS";

    // TODO change this to where you want to put your files.
    private static final String PATH = "/tmp/martin";

    private static ConcurrentHashMap<String, String> locks;

    static {
        try {
            locks = getFromFile();
        } catch (Exception e) {
            locks = new ConcurrentHashMap<>();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        String serverHostname = "127.0.0.1";
        if (args.length > 0) {
            serverHostname = args[0];
        }
        server.run(serverHostname);
    }

    public Server() {
        super();
    }

    AuthServerInterface authServerStub;

    private void run(String serverHostname) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            ServerInterface stub = (ServerInterface) UnicastRemoteObject
                    .exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("server", stub);
            authServerStub = loadAuthServerStub(serverHostname);
            System.out.println("Server ready.");
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    private static void saveToFile() throws IOException {
        File outputFile = new File(FILE_NAME);
        ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(outputFile));
        objectOut.writeObject(locks);
    }

    private static ConcurrentHashMap<String, String> getFromFile() throws IOException, ClassNotFoundException {
        ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(FILE_NAME));
        return (ConcurrentHashMap<String, String>) objectIn.readObject();
    }

    private static AuthServerInterface loadAuthServerStub(String hostname) {
        AuthServerInterface stub = null;

        try {
            Registry registry = LocateRegistry.getRegistry(hostname);
            stub = (AuthServerInterface) registry.lookup("auth");
        } catch (NotBoundException e) {
            System.out.println("Erreur: Le nom '" + e.getMessage() + "' n'est pas défini dans le registre.");
        } catch (RemoteException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

        return stub;
    }

    @Override
    public void create(CreateMessage createMessage) throws RuntimeException {
        verifyLogin(createMessage.getLogin(), createMessage.getPassword());

        File f = new File(PATH + File.separator + createMessage.getName());
        if (f.exists()) {
            throw new RuntimeException("Un fichier avec ce nom existe déjà.");
        }

        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
            f.setReadable(true);
        } catch (IOException e) {
            throw new RuntimeException("Une erreur est survenue lors de la création du fichier.");
        }
    }

    @Override
    public List<FileInfo> list(Message message) {
        verifyLogin(message.getLogin(), message.getPassword());

        File f = new File(PATH);
        f.getParentFile().mkdirs();
        if (f.list() == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(f.list())
                .parallelStream()
                .map((file) -> new FileInfo(file, locks.get(file)))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDetails> syncLocalDirectory(Message message) {
        verifyLogin(message.getLogin(), message.getPassword());

        File f = new File(PATH);
        f.getParentFile().mkdirs();
        if (f.list() == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(f.listFiles())
                .filter(file -> file.canRead())
                .map((file -> {
                    try {
                        return new FileDetails(file.getName(), locks.get(file), Files.readAllBytes(file.toPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }))
                .collect(Collectors.toList());
    }

    @Override
    public FileDetails get(NameChecksumMessage nameChecksumMessage) throws RuntimeException {
        verifyLogin(nameChecksumMessage.getLogin(), nameChecksumMessage.getPassword());

        try {
            File f = new File(PATH + File.separator + nameChecksumMessage.getName());
            if (!f.exists()) {
                throw new RuntimeException("Le fichier spécifié n'existe pas.");
            }

            byte[] fileContent = Files.readAllBytes(f.toPath());

            if (Arrays.equals(nameChecksumMessage.getChecksum(), Utils.getMD5(fileContent))) {
                throw new RuntimeException("Le fichier n'a pas changé.");
            }

            return new FileDetails(f.getName(), locks.get(f.getName()), fileContent);

        } catch (IOException e) {
            throw new RuntimeException("Une erreur est survenue lors de l'obtention du fichier.");
        }
    }

    @Override
    public FileDetails lock(NameChecksumMessage nameChecksumMessage) throws RuntimeException {
        verifyLogin(nameChecksumMessage.getLogin(), nameChecksumMessage.getPassword());
        synchronized (locks) {
            if (locks.containsKey(nameChecksumMessage.getName())) {
                throw new RuntimeException("Le fichier est déjà verrouillé par l'usager " + locks.get(nameChecksumMessage.getName()));
            }

            locks.put(nameChecksumMessage.getName(), nameChecksumMessage.getLogin());
            try {
                saveToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return get(nameChecksumMessage);
    }

    @Override
    public void push(NameContentMessage nameContentMessage) throws RuntimeException {
        verifyLogin(nameContentMessage.getLogin(), nameContentMessage.getPassword());

        if (!locks.containsKey(nameContentMessage.getName()) || !locks.get(nameContentMessage.getName()).equals(nameContentMessage.getLogin())) {
            throw new RuntimeException("Le fichier doit être verrouillé avant d'être modifié.");
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PATH + File.separator + nameContentMessage.getName());
            fileOutputStream.write(nameContentMessage.getContent());
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Une erreur est survenue lors de la sauvegarde du fichier.");
        }

        locks.remove(nameContentMessage.getName());
        try {
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyLogin(String login, String password) {
        try {
            if (!authServerStub.verify(new Message(login, password))) {
                throw new RuntimeException("Vous devez être authentifié pour effectuer cette commande.");
            }
        } catch (RemoteException e) {
            throw new RuntimeException("Vous devez être authentifié pour effectuer cette commande.");
        }
    }
}

