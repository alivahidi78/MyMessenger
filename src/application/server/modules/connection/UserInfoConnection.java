package application.server.modules.connection;

import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class UserInfoConnection extends ConstantConnection {

    public UserInfoConnection(ObjectInputStream in, ObjectOutputStream out) {
        super(in, out, null);
    }

    @Override
    protected void monitor() {
        boolean connected = true;
        while (connected) {
            try {
                Long id = (Long) in.readObject();
                Optional<User> user = db.findUserByID(id);
                if (user.isPresent())
                    out.writeObject(user.get().getSimpleUser());
                else
                    out.writeObject(null);//FIXME
                out.flush();
            } catch (IOException e) {
                connected = false;
            } catch (ClassNotFoundException e) {
                connected = false;
                e.printStackTrace();
            }
        }
    }
}
