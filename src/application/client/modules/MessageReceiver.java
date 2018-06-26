package application.client.modules;

import application.client.controllers.GraphicController;
import application.util.message.Message;
import application.util.message.MessageType;
import application.util.message.info.GroupAdditionInfoMessage;
import application.util.message.info.InfoMessageType;
import application.util.message.info.ServerInfoMessage;
import application.util.message.info.UserStatusInfoMessage;
import application.util.user.Info;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MessageReceiver {
    private ObjectInputStream in;
    private Thread thread;
    private volatile boolean connected;
    private volatile boolean safelyDisconnected = false;

    public MessageReceiver(ObjectInputStream input) {
        this.in = input;
        thread = new Thread(() -> {
            connected = true;
            while (connected) {
                try {
                    Message message = (Message) in.readObject();
                    if (message.type == MessageType.SERVER_INFO) {
                        processServerMessage((ServerInfoMessage) message);
                    } else {
                        Cache.addMessage(message);
                        GraphicController.loadMessageGraphics(message);
                    }
                } catch (IOException e) {
                    connected = false;
                    if (!safelyDisconnected)
                        GraphicController.showDisconnectedAlert();
                } catch (ClassNotFoundException e) {
                    connected = false;
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
    }

    private void processServerMessage(ServerInfoMessage message) {
        if (message.infoType == InfoMessageType.UPDATE_USER_STATUS) {
            UserStatusInfoMessage msg = (UserStatusInfoMessage) message;
            Info user = LogicalEventHandler.getUserInfo(msg.id);
            user.setOnline(msg.isOnline);
            user.setLastSeen(msg.lastSeen);
            GraphicController.updateUserInfoBar(user);
        }
        if(message.infoType == InfoMessageType.ADDITION_TO_GROUP){
            GroupAdditionInfoMessage msg = (GroupAdditionInfoMessage) message;
            Info group = LogicalEventHandler.getUserInfo(msg.groupID);
            GraphicController.loadUserInfoToList(group);
        }
    }

    public void start() {
        thread.start();
    }

    public void stop() throws IOException {
        connected = false;
        safelyDisconnected = true;
        in.close();
    }
}
