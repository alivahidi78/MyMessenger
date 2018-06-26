package application.client.modules;

import application.util.message.Message;
import application.util.user.Info;
import application.util.user.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private static User currentUser;
    private static Map<Long, List<Message>> chats;
    private static Map<Long, Info> usersInfo = new ConcurrentHashMap<>();

    public static void loadChats(Map<Long, List<Message>> chats) {
        Cache.chats = chats;
    }

    public static Map<Long, List<Message>> getChats() {
        return chats;
    }

    public static List<Message> getChat(long id) {
        return chats.get(id);
    }

    public static void addMessage(Message message) {
        if (message.isFromGroup) {
            if (chats.get(message.group) == null) {
                LinkedList<Message> chat = new LinkedList<>();
                Cache.chats.put(message.group, chat);
            }
            chats.get(message.group).add(message);
        } else {
            if (chats.get(message.sender) == null) {
                LinkedList<Message> chat = new LinkedList<>();
                Cache.chats.put(message.sender, chat);
            }
            chats.get(message.sender).add(message);
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Cache.currentUser = currentUser;
    }

    static void clear() {
        currentUser = null;
        chats = null;
        usersInfo = new ConcurrentHashMap<>();
        //TODO
    }

    static Info getUserInfoFromCache(long id) {
        return usersInfo.get(id);
    }

    static void loadUserInfoToCache(Info user) {
        usersInfo.put(user.getID(), user);
    }
}
