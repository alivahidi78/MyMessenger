package application.server.modules;

import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Handles the interactions of the program with files.
 */
class FileHandler<T> {
    void saveObjectToFile(T data, String path) {//TODO run from separate thread
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Optional<T> readObjectFromFile(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            //noinspection unchecked
            return Optional.of((T) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    static void saveFileFromStream(InputStream stream, String path) throws Exception {
        byte[] buffer = new byte[1024];
        DataInputStream inputStream = new DataInputStream(stream);
        File file = Paths.get(path).toAbsolutePath().toFile();
        OutputStream o = new FileOutputStream(file);
        int read;
        while ((read = inputStream.read(buffer)) > 0) {
            o.write(buffer, 0, read);
        }
        o.close();
    }

    static void sendFileToStream(OutputStream stream, String path) throws Exception {
        DataOutputStream outputStream = new DataOutputStream(stream);
        File file = Paths.get(path).toAbsolutePath().toFile();
        FileInputStream i = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = i.read(buffer)) > 0) {
            Thread.sleep(1); ///todo remove
            outputStream.write(buffer, 0, read);
        }
        outputStream.close();
    }

}
