package application.server.modules.connection;

import application.util.user.SimpleUser;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SearchConnection extends ConstantConnection {
    public SearchConnection(ObjectInputStream in, ObjectOutputStream out, User user) {
        super(in, out, user);
    }

    @Override
    protected void monitor() {
        boolean connected = true;
        while (connected) {
            try {
                String search = (String) in.readObject();
                List<SimpleUser> foundUsers = db.searchFor(user,search);
                out.writeObject(foundUsers);
                out.flush();
            } catch (IOException e) {
                connected = false;
                disconnect();
            } catch (ClassNotFoundException e) {
                connected = false;
                e.printStackTrace();
            }
        }
    }
}
