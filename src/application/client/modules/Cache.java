package application.client.modules;

import application.util.message.Message;
import application.util.user.SimpleUser;
import application.util.user.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static User currentUser;
    public static Map<Long, List<Message>> chats;
    public static Map<Long, SimpleUser> usersInfo = new ConcurrentHashMap<>();

    public static void clear() {
        currentUser = null;
        chats = null;
        usersInfo = new ConcurrentHashMap<>();
        //TODO
    }
}
