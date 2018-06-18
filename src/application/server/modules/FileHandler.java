package application.server.modules;

import java.io.*;
import java.util.Optional;
/**
 * Handles the interactions of the program with files.
 * */
public class FileHandler {
    private static final String PATH = "data";
    private static FileHandler instance = new FileHandler();

    private FileHandler() {
    }

    public static FileHandler getInstance() {
        return instance;
    }

    public void saveData(Database db) {//TODO run from separate thread
        try (FileOutputStream fileOutputStream = new FileOutputStream(PATH);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<Database> readData() {
        try (FileInputStream fileInputStream = new FileInputStream(PATH);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return Optional.of((Database) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
