package application.client.modules;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;

import java.io.*;
import java.nio.file.Paths;

/**
 * Handles the interactions of the program with files.
 */
class FileHandler {
    static void saveFileFromStream(InputStream stream, String path, LongProperty totalDownload) throws Exception {
        byte[] buffer = new byte[1024];
        File file = Paths.get(path).toAbsolutePath().toFile();
        OutputStream o = new FileOutputStream(file);
        DataInputStream inputStream = new DataInputStream(stream);
        totalDownload.setValue(0);
        int read, totalDownloaded = 0;
        while ((read = inputStream.read(buffer)) > 0) {
            o.write(buffer, 0, read);
            totalDownloaded += read;
            totalDownload.setValue(totalDownloaded);
        }
        o.close();
    }

    static void sendFileToStream(OutputStream stream, String path, DoubleProperty totalUpload) throws Exception {
        File file = Paths.get(path).toAbsolutePath().toFile();
        FileInputStream i = new FileInputStream(file);
        DataOutputStream outputStream = new DataOutputStream(stream);
        byte[] buffer = new byte[1024];
        int read, totalUploaded = 0;
        while ((read = i.read(buffer)) > 0) {
            Thread.sleep(1); ///todo remove
            outputStream.write(buffer, 0, read);
            totalUploaded += read;
            totalUpload.setValue((double)(totalUploaded)/file.length());
        }
        outputStream.close();
    }
}
