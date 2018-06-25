package application.server.modules.connection;

import application.util.message.Message;
import application.util.message.info.UserStatusInfoMessage;
import application.util.user.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Optional;

public class MessagingConnection extends ConstantConnection {


    public MessagingConnection(ObjectInputStream in, ObjectOutputStream out, User user) {
        super(in, out, user);
    }

    @Override
    protected void monitor() {
        boolean connected = true;
        while (connected) {
            try {
                Message message = (Message) in.readObject();
                db.processMessage(message);
                this.processMessage(message);
            } catch (IOException e) {
                connected = false;
                disconnect();
                //disconnected
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        //TODO
    }

    @Override
    public void connect() {
        super.connect();
        user.setOnline(this);
        Message message = new UserStatusInfoMessage(user.getAssociates(),
                new Date(), true, user.getID(), new Date());
        processMessage(message);
    }

    @Override
    protected void disconnect() {
        user.setOffline();
        super.disconnect();
        Message message = new UserStatusInfoMessage(user.getAssociates(),
                new Date(), false, user.getID(), new Date());
        processMessage(message);
    }

    private void processMessage(Message message) {
        Thread thread = new Thread(() -> {
            for (Long target : message.targets) {
                Optional<User> user = db.findUserByID(target);
                if (user.isPresent() && user.get().isOnline() && user.get().getConnection() != null) {
                    user.get().getConnection().sendMessage(message);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private synchronized void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
