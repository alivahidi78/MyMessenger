package application.client.modules;

import application.util.message.Message;
import application.util.user.SimpleUser;
import application.util.user.User;

import java.io.IOException;
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

    public static SimpleUser getUserInfo(long id) {
        SimpleUser userInfo = usersInfo.get(id);
        if (userInfo != null)
            return userInfo;
        else
            return getUserInfoFromServer(id);
    }

    public static SimpleUser getUserInfoFromServer(long id){
        SimpleUser userInfo;
        try {
            userInfo = Network.getUserInfo(id);
            usersInfo.put(id, userInfo);
            return userInfo;
        } catch (IOException e) {
            return null;
        }
    }
}
