package ca.polymtl.inf8480.tp1.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ca.polymtl.inf8480.tp1.shared.ServerInterface;
import ca.polymtl.inf8480.tp1.shared.Utils;
import ca.polymtl.inf8480.tp1.shared.domain.FileDetails;
import ca.polymtl.inf8480.tp1.shared.domain.FileInfo;
import ca.polymtl.inf8480.tp1.shared.messages.CreateMessage;
import ca.polymtl.inf8480.tp1.shared.messages.NameChecksumMessage;
import ca.polymtl.inf8480.tp1.shared.messages.NameContentMessage;

public class Server implements ServerInterface {

    private static final String PATH = "/tmp/martin";

    private static final ConcurrentHashMap<String, String> locks = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    public Server() {
        super();
    }

    private void run() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            ServerInterface stub = (ServerInterface) UnicastRemoteObject
                    .exportObject(this, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("server", stub);
            System.out.println("Server ready.");
        } catch (ConnectException e) {
            System.err
                    .println("Impossible de se connecter au registre RMI. Est-ce que rmiregistry est lancé ?");
            System.err.println();
            System.err.println("Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }

    @Override
    public void create(CreateMessage createMessage) throws RemoteException {
        File f = new File(PATH + File.separator + createMessage.getName());
        if (f.exists()) {
            throw new RemoteException("Un fichier avec ce nom existe déjà.");
        }

        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (IOException e) {
            throw new RemoteException("Une erreur est survenue lors de la création du fichier.");
        }
    }

    @Override
    public List<FileInfo> list() throws RemoteException {
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
    public List<FileDetails> syncLocalDirectory() throws RemoteException {
        File f = new File(PATH);
        f.getParentFile().mkdirs();
        if (f.list() == null) {
            return new ArrayList<>();
        }

        return Arrays.asList(f.listFiles())
                .parallelStream()
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
    public FileDetails get(NameChecksumMessage nameChecksumMessage) throws RemoteException {
        try {
            File f = new File(PATH + File.separator + nameChecksumMessage.getName());
            byte[] fileContent = Files.readAllBytes(f.toPath());
            if (!f.exists()) {
                throw new RemoteException("Le fichier spécifié n'existe pas.");
            }

            if (Arrays.equals(nameChecksumMessage.getChecksum(), Utils.getMD5(fileContent))) {
                throw new RemoteException("Le fichier n'a pas changé.");
            }

            return new FileDetails(f.getName(), locks.get(f.getName()), Files.readAllBytes(f.toPath()));

        } catch (RemoteException e) {
            throw e;
        } catch (IOException e) {
            throw new RemoteException("Une erreur est survenue lors de l'obtention du fichier.");
        }
    }

    @Override
    public FileDetails lock(NameChecksumMessage nameChecksumMessage) throws RemoteException {
        synchronized (locks) {
            if (locks.containsKey(nameChecksumMessage.getName())) {
                throw new RemoteException("Le fichier est déjà verrouillé par l'usager " + locks.get(nameChecksumMessage.getName()));
            }

            locks.put(nameChecksumMessage.getName(), nameChecksumMessage.getLogin());
        }

        return get(nameChecksumMessage);
    }

    @Override
    public void push(NameContentMessage nameContentMessage) throws RemoteException {
        if (!locks.containsKey(nameContentMessage) || !locks.get(nameContentMessage.getName()).equals(nameContentMessage.getLogin())) {
            throw new RemoteException("Le fichier doit être verrouillé avant d'être modifié.");
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PATH + File.separator + nameContentMessage.getName());
            fileOutputStream.write(nameContentMessage.getContent());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RemoteException("Une erreur est survenue lors de la sauvegarde du fichier.");
        }

        locks.remove(nameContentMessage.getName());
    }

    private boolean verifyLogin(String login, String password) {
        // TODO: Return something else.
        return true;
    }
}

