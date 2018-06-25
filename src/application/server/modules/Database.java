package application.server.modules;

import application.util.message.Message;
import application.util.user.SimpleUser;
import application.util.user.User;
import javafx.scene.image.Image;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Holds all the client information on the server.
 */
public class Database implements Serializable {
    private static final Database instance;
    private static FileHandler<Database> fh = new FileHandler<>();

    static {
        Optional<Database> databaseOptional = fh.readData("data/DATA");
        instance = databaseOptional.orElseGet(Database::new);
    }

    private int nextPermID = 0;
    private ConcurrentLinkedDeque<User> users = new ConcurrentLinkedDeque<>();

    private Database() {
    }

    public static Database getInstance() {
        return instance;
    }

    private void saveData() {
        fh.saveData(instance, "data/DATA");
    }

    public Optional<User> findUserByUsername(String username) {
        for (User user : users) {
            if (user.usernameEquals(username))
                return Optional.of(user);
        }
        return Optional.empty();
    }

    public void createNewUser(String name, String username, String password) {
        User user;
        synchronized (instance) {
            user = new User(nextPermID, name, username, password);
            nextPermID++;
        }
        users.add(user);
//      user.setImage(); TODO default image
        saveData();
    }

    public List<SimpleUser> searchFor(String search) {
        var result = new ArrayList<SimpleUser>();
        for (User user : users) {
            if ((!search.isEmpty()) && user.getUsername().startsWith(search))
                result.add(user.getSimpleUser());
        }
        return result;
    }

    public void processMessage(Message message) {
        Thread thread = new Thread(()->{
            for (Long target: message.targets){
                Optional<User> user = instance.findUserByID(target);
                if(user.isPresent()){
                    user.get().addAssociate(message.sender);
                    user.get().addMessage(message.sender,message);
                    instance.findUserByID(message.sender).get().addMessage(target,message);
                }
            }
            saveData();
        });
        thread.setDaemon(true);
        thread.start();
    }

    public Optional<User> findUserByID(long target) {
        for (User user : users) {
            if(user.getID()==target){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
