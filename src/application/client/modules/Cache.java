package application.client.modules;

import application.util.user.User;

public class Cache {
    public static User currentUser;

    public static void clear() {
        currentUser = null;
        //TODO
    }
}
