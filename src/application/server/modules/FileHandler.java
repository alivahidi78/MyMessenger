package application.server.modules;

import java.io.*;
import java.util.Optional;

/**
 * Handles the interactions of the program with files.
 */
class FileHandler<T> {
    void saveData(T data,String path) {//TODO run from separate thread
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Optional<T> readData(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            //noinspection unchecked
            return Optional.of((T) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }
}
