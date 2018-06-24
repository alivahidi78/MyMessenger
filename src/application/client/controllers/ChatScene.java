package application.client.controllers;

import application.client.modules.Cache;
import application.client.modules.GraphicEventHandler;
import application.util.user.SimpleUser;
import application.util.user.User;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChatScene extends MainController implements Initializable {
    public TextArea sendMessageTextArea;
    public ListView messageLog;
    public ListView contactList;
    public Menu userMenuButton;
    public TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
        userMenuButton.setText(Cache.currentUser.getName());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<SimpleUser> list = GraphicEventHandler.searchFor(newValue);
            contactList.setItems(FXCollections.observableArrayList(list));
            //TODO
        });

    }

    public void sendMessage() {
        //TODO
//        sendMessageTextArea.clear();
    }

    public void openSettings() {
        //TODO
    }

    public void signOut() {
        Cache.clear();
        goTo("StartUp");
    }

    public void close() {
        System.exit(0);
    }
}
