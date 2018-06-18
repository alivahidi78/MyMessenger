package application.server.modules;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Connection {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public Connection(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }
}
