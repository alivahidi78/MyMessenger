package application.server.modules;

import application.util.message.Message;
import application.util.user.Group;
import application.util.user.Info;
import application.util.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Holds all the client information on the server.
 */
public class Database implements Serializable {
    private static final Database instance;
    private static FileHandler<Database> fh = new FileHandler<>();

    static {
        Optional<Database> databaseOptional = fh.readObjectFromFile("data/DATA");
        instance = databaseOptional.orElseGet(Database::new);
    }

    private int nextPermID = 0;
    private ConcurrentLinkedDeque<User> users = new ConcurrentLinkedDeque<>();
    private ConcurrentLinkedDeque<Group> groups = new ConcurrentLinkedDeque<>();

    private Database() {
        users.forEach(User::resetOnlineState);
    }

    public static Database getInstance() {
        return instance;
    }

    private void saveData() {
        fh.saveObjectToFile(instance, "data/DATA");
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
        saveData();
    }

    public List<Info> searchFor(User requester, String search) {
        var result = new ArrayList<Info>();
        for (User user : users) {
            if ((!search.isEmpty()) && user.getUsername().startsWith(search)
                    && !user.equals(requester))
                result.add(user.getInfo());

            //associate
            user.addAssociate(requester.getID());
        }
        return result;
    }

    public void processMessage(Message message) {
        Thread thread = new Thread(() -> {
            long target = message.target;
            Optional<User> user = findUserByID(target);
            Optional<Group> group = findGroupByID(target);
            if (user.isPresent()) {
                user.get().addAssociate(message.sender);//associate
                findUserByID(message.sender).get().addAssociate(target);
                user.get().addMessage(message.sender, message);
                findUserByID(message.sender).get().addMessage(target, message);
            }
            if (group.isPresent()) {
                for (long member : group.get().getMembers()) {
                    findUserByID(member).get().addMessage(message.group, message);
                }
            }
            saveData();
        });
        thread.setDaemon(true);
        thread.start();
    }

    public Optional<User> findUserByID(long target) {
        for (User user : users) {
            if (user.getID() == target) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<Group> findGroupByID(long target) {
        for (Group group : groups) {
            if (group.getID() == target)
                return Optional.of(group);
        }
        return Optional.empty();
    }

    public long createGroup(String name, Set<Long> members) {
        Group group;
        long id;
        synchronized (instance) {
            id = nextPermID;
            group = new Group(id, name, members);
            groups.add(group);
            nextPermID++;
        }
        Thread thread = new Thread(() -> {
            for (long member : members) {
                Optional<User> user = findUserByID(member);
                user.ifPresent(user1 -> user1.addEmptyChat(id));
            }
        });
        thread.setDaemon(true);
        thread.start();
        return id;
    }
}
