package application.server.modules;

import application.util.user.User;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Holds all the client information on the server.
 */
public class Database implements Serializable {
    private static Database instance;
    private static FileHandler fh = FileHandler.getInstance();

    static {
        Optional<Database> databaseOptional = fh.readData();
        instance = databaseOptional.orElseGet(Database::new);
    }

    private ConcurrentLinkedDeque<User> users = new ConcurrentLinkedDeque<>();

    private Database() {
    }

    public static Database getInstance() {
        return instance;
    }

    private void saveData() {
        fh.saveData(instance);
    }

    public Optional<User> findUserByUsername(String username) {
        for (User user : users) {
            if (user.usernameEquals(username))
                return Optional.of(user);
        }
        return Optional.empty();
    }

    public void addUser(User user) {
        users.add(user);
        saveData();
    }
}
