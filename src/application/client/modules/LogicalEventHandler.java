package application.client.modules;

import application.client.controllers.GraphicController;
import application.util.answer.Answer;
import application.util.message.FileMessage;
import application.util.message.Message;
import application.util.request.GroupCreationRequest;
import application.util.user.Info;
import javafx.beans.property.DoubleProperty;

import java.io.File;
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
        GraphicController.clear();
    }

    public static void sendMessage(Message message) {
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

    public static void sendFileToServer(FileMessage message, File file, DoubleProperty property) throws Exception {
        Network.sendFile(message, file, property);
    }
}
