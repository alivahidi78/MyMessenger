package application.client.modules;

import application.util.answer.Answer;
import application.util.message.TextMessage;
import application.util.request.GroupCreationRequest;
import application.util.user.Info;

import java.io.IOException;
import java.util.Set;

public class LogicalEventHandler {
    public static Info getUserInfo(long id) {
        Info info = Cache.getUserInfoFromCache(id);
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
        try {
            Network.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Cache.clear();
    }

    public static void sendTextMessage(TextMessage message) {
        try {
            Network.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Answer requestGroupCreation(String name, Set<Long> groupMembers) {
        groupMembers.add(Cache.getCurrentUser().getID());
        return Network.request(new GroupCreationRequest(Cache.getCurrentUser().getUsername(),
                Cache.getCurrentUser().getPassword(), name, groupMembers));
    }
}
