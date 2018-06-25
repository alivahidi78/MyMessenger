package application.client.modules;

import application.client.ClientMain;
import application.client.controllers.ContactCell;
import application.util.answer.Answer;
import application.util.answer.AnswerType;
import application.util.answer.SignInAcceptedAnswer;
import application.util.message.Message;
import application.util.message.TextMessage;
import application.util.request.MessagingConnectionRequest;
import application.util.request.SearchConnectionRequest;
import application.util.request.SignUpRequest;
import application.util.request.UserInfoConnectionRequest;
import application.util.user.SimpleUser;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphicEventHandler {
    public static ObservableList<SimpleUser> contactList;
    public static SimpleUser chattingUser;
    public static ObservableList<Message> messages;
    public static List<Message> currentChat;
    public static Map<Long, ContactCell> userCellMap;
    public static Label nameField;
    public static Label idField;
    public static Label lastSeenField;

    public static Answer requestSignIn(String username, String password) {
        Answer answer = Network.request(new MessagingConnectionRequest(username, password));
        if (answer.type == AnswerType.SIGN_IN_ACCEPTED) {
            Cache.currentUser = ((SignInAcceptedAnswer) answer).user;
            Cache.chats = ((SignInAcceptedAnswer) answer).user.getChats();
            initiateConnections();
        }
        return answer;
    }

    private static void initiateConnections() {
        Answer SearchConAnswer = Network.request(new SearchConnectionRequest(
                Cache.currentUser.getUsername(), Cache.currentUser.getPassword()));
        Answer userInfoAnswer = Network.request(new UserInfoConnectionRequest(Cache.currentUser.getUsername(),
                Cache.currentUser.getPassword()));
        //TODO ???
    }


    public static Answer requestSignUp(String name, String username, String password, Image userImg) {
        Answer answer = Network.request(new SignUpRequest(name, username, password));
        return answer;
    }

    public static List<SimpleUser> searchFor(String s) {
        try {
            return Network.getSearchResult(s);
        } catch (IOException e) {
            return new ArrayList<>();
            //TODO ERROR
        }
    }

    public static SimpleUser getUserInfo(long id) {
        return Cache.getUserInfo(id);
    }

    public static Image choosePicture(String defaultImgPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All Images", "*.*"));//TODO list extensions
        try {
            File file = fileChooser.showOpenDialog(ClientMain.getStage());
            return new Image("file:" + file.getAbsolutePath());
        } catch (NullPointerException e) {
            return new Image(defaultImgPath);
        }
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

    public static void reloadCache(Message message) {
        Platform.runLater(() -> {//TODO null
            if (!contactList.contains(getUserInfo(message.sender))) {
                contactList.add(getUserInfo(message.sender));
            }
            if (chattingUser.getID() == message.sender) {
                currentChat.add(message);
                messages.add(message);
            }
        });
    }

    public static void showDisconnectedAlert() {
        Platform.runLater(() -> {
            boolean connected = false;
            Alert alert = new Alert(Alert.AlertType.WARNING, "Disconnected from server!",
                    new ButtonType("Try to connect"));
            alert.getDialogPane().getStylesheets().add(ClientMain.class.getResource
                    ("views/stylesheets/Alert.css").toExternalForm());
            while (!connected) {
                alert.showAndWait();
                Answer answer = GraphicEventHandler.requestSignIn(Cache.currentUser.getUsername(),
                        Cache.currentUser.getPassword());
                if (answer.requestAccepted) {
                    connected = true;
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Reconnected!");
                    alert2.getDialogPane().getStylesheets().add(ClientMain.class.getResource
                            ("views/stylesheets/Alert.css").toExternalForm());
                    alert2.show();
                }
            }
        });
    }

    public static void updateUserInfoBar(SimpleUser user) {
        Platform.runLater(() -> {
            if (user == null) {
                nameField.setText("");
                idField.setText("");
                lastSeenField.setText("");
            } else if (chattingUser.equals(user)) {
                nameField.setText(user.getName());
                idField.setText("@" + user.getUsername());
                if (user.isOnline())
                    lastSeenField.setText("Online");
                else
                    lastSeenField.setText("Last Seen at " + user.getLastSeen());
            }
        });

    }
}
