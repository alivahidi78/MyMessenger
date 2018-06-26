package application.server.modules.connection;

import application.util.message.Message;
import application.util.message.info.UserStatusInfoMessage;
import application.util.user.Group;
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

    public static void processMessage(Message message) {
        Thread thread = new Thread(() -> {
            long target = message.target;
            Optional<User> user = db.findUserByID(target);
            Optional<Group> group = db.findGroupByID(target);
            if (user.isPresent() && user.get().isOnline() && user.get().getConnection() != null) {
                user.get().getConnection().sendMessage(message);
            }
            if (group.isPresent()) {
                for (long member : group.get().getMembers()) {
                    Optional<User> receiver = db.findUserByID(member);
                    if (receiver.isPresent() && member != message.sender && receiver.get().isOnline()
                            && receiver.get().getConnection() != null) {
                        receiver.get().getConnection().sendMessage(message);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void monitor() {
        boolean connected = true;
        while (connected) {
            try {
                Message message = (Message) in.readObject();
                db.processMessage(message);
                processMessage(message);
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
        user.getAssociates().forEach((user1) -> {
            Message message = new UserStatusInfoMessage(user1,
                    new Date(), true, user.getID(), new Date());
            processMessage(message);
        });
    }

    @Override
    protected void disconnect() {
        user.setOffline();
        super.disconnect();
        user.getAssociates().forEach((user1) -> {
            Message message = new UserStatusInfoMessage(user1,
                    new Date(), false, user.getID(), new Date());
            processMessage(message);
        });
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
