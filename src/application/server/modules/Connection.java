package application.server.modules;

import application.util.user.User;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Connection {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User user;

    Connection(ObjectInputStream in, ObjectOutputStream out, User user) {
        this.in = in;
        this.out = out;
        this.user = user;
        user.setOnline();
    }

    ObjectOutputStream getOutput() {
        return out;
    }

    ObjectInputStream getInput() {
        return in;
    }

    void startReceiving() {
        //TODO
    }

    void disconnect() {
        user.setOffline();
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }
}
