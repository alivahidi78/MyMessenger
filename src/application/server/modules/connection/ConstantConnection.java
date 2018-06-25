package application.server.modules.connection;

import application.server.modules.Database;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class ConstantConnection {
    ObjectOutputStream out;
    ObjectInputStream in;
    User user;
    Database db = Database.getInstance();

    ConstantConnection(ObjectInputStream in, ObjectOutputStream out, User user) {
        this.in = in;
        this.out = out;
        this.user = user;
    }

    ObjectOutputStream getOutput() {
        return out;
    }

    ObjectInputStream getInput() {
        return in;
    }

    public User getUser() {
        return user;
    }

    protected abstract void monitor();

    public void connect() {
        Thread thread = new Thread(this::monitor);
        thread.setDaemon(true);
        thread.start();
    }

    protected void disconnect() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
