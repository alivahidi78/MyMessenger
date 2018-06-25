package application.client.controllers;

import application.client.ClientMain;
import application.client.modules.Cache;
import application.client.modules.LogicalEventHandler;
import application.client.modules.Network;
import application.util.answer.Answer;
import application.util.message.Message;
import application.util.request.SignUpRequest;
import application.util.user.SimpleUser;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GraphicController {
    static ObservableList<SimpleUser> contactList;
    static SimpleUser chattingUser;
    static ObservableList<Message> messages;
    static List<Message> currentChat;
    static Label nameField;
    static Label idField;
    static Label lastSeenField;
    static ListView contactListView;

    static void goTo(String name) {
        try {
            ClientMain.getScene().setRoot(FXMLLoader.load(ClientMain.class.getResource("views/fxml/" + name + ".fxml")));
            ClientMain.getScene().getStylesheets().clear();
            ClientMain.getScene().getStylesheets().add(ClientMain.class.getResource("views/stylesheets/" + name + ".css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Alert getNewAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType, text);
        alert.getDialogPane().getStylesheets().add(ClientMain.class.getResource("views/stylesheets/Alert.css").toExternalForm());
        return alert;
    }

    static Answer requestSignIn(String username, String password) {
        return Network.initiateConnections(username, password);
    }


    static Answer requestSignUp(String name, String username, String password, Image userImg) {
        return Network.request(new SignUpRequest(name, username, password));
    }

    static List<SimpleUser> searchFor(String s) {
        try {
            return Network.getSearchResult(s);
        } catch (IOException e) {
            return new ArrayList<>();
            //TODO ERROR
        }
    }

    static Image choosePicture(String defaultImgPath) {
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

    public static void loadMessageGraphics(Message message) {
        Platform.runLater(() -> {
            SimpleUser info = LogicalEventHandler.getUserInfo(message.sender);
            if (!contactList.contains(info)) {
                contactList.add(info);
            }

            if (chattingUser != null && chattingUser.getID() == message.sender) {
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
                Answer answer = requestSignIn(Cache.getCurrentUser().getUsername(),
                        Cache.getCurrentUser().getPassword());
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
            } else if (chattingUser != null && chattingUser.equals(user)) {
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
