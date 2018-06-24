package application.client.modules;

import application.util.message.Message;
import application.util.user.User;

import java.util.List;
import java.util.Map;

public class Cache {
    public static User currentUser;
    public static Map<Long, List<Message>> chats;

    public static void clear() {
        currentUser = null;
        //TODO
    }
}
