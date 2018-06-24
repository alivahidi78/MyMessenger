package application.client.controllers;

import application.client.modules.Cache;
import application.client.modules.GraphicEventHandler;
import application.util.user.SimpleUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

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
        ObservableList<SimpleUser> userList = FXCollections.observableArrayList();
        contactList.setItems(userList);
        //TODO
        userMenuButton.setText(Cache.currentUser.getName());
        contactList.setCellFactory(new Callback<ListView<SimpleUser>, ListCell<SimpleUser>>() {
            @Override
            public ListCell<SimpleUser> call(ListView<SimpleUser> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(SimpleUser user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null && !empty) {
                            ContactCell cell = new ContactCell();
                            cell.setInfo(user);
                            setGraphic(cell.getBox());
                        }else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<SimpleUser> list = GraphicEventHandler.searchFor(newValue);
            userList.clear();
            userList.addAll(list);
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
        GraphicEventHandler.signOut();
        goTo("StartUp");
    }

    public void close() {
        System.exit(0);
    }
}
