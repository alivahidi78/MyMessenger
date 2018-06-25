package application.client.modules;

import application.util.message.TextMessage;
import application.util.user.SimpleUser;

import java.io.IOException;

public class LogicalEventHandler {
    public static SimpleUser getUserInfo(long id) {
        SimpleUser info = Cache.getUserInfoFromCache(id);
        if (info == null) {
            try {
                info = Network.getUserInfoFromServer(id);
                Cache.loadUserInfoToCache(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return info;
    }

    public static void signOut() {
        Cache.clear();
        try {
            Network.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendTextMessage(TextMessage message) {
        try {
            Network.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
