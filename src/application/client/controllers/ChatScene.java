package application.client.controllers;

import application.client.modules.Cache;
import application.client.modules.GraphicEventHandler;
import application.util.message.Message;
import application.util.message.TextMessage;
import application.util.user.SimpleUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

import static application.client.modules.GraphicEventHandler.*;

public class ChatScene extends MainController implements Initializable {
    public TextArea sendMessageTextArea;
    public ListView messageLogListView;
    public ListView contactListView;
    public Menu userMenuButton;
    public TextField searchField;
    public Label nameField;
    public Label idField;
    public Label lastSeenField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GraphicEventHandler.nameField = nameField;
        GraphicEventHandler.idField = idField;
        GraphicEventHandler.lastSeenField = lastSeenField;
        updateUserInfoBar(null);
        ObservableList<SimpleUser> searchList = FXCollections.observableArrayList();
        contactList = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        userCellMap = new LinkedHashMap<>();
        messageLogListView.setItems(messages);
        contactListView.setItems(contactList);

        searchList.clear();
        for (Long id : Cache.chats.keySet())
            contactList.add(GraphicEventHandler.getUserInfo(id));
        //TODO
        userMenuButton.setText(Cache.currentUser.getName());
        contactListView.setCellFactory(new Callback<ListView<SimpleUser>, ListCell<SimpleUser>>() {
            @Override
            public ListCell<SimpleUser> call(ListView<SimpleUser> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(SimpleUser user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null && !empty) {
                            ContactCell cell = new ContactCell();
                            userCellMap.put(user.getID(), cell);
                            cell.setInfo(user);
                            setGraphic(cell.getBox());
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        messageLogListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);
                        if (message != null && !empty) {
                            MessageCell cell = new MessageCell();
                            cell.setInfo(message);
                            setGraphic(cell.getBox());
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        contactListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chattingUser = (SimpleUser) newValue;
            messages.clear();
            if (chattingUser != null) {
                currentChat = Cache.chats.get(chattingUser.getID());
                if (currentChat != null)
                    messages.addAll(Cache.chats.get(chattingUser.getID()));
                updateUserInfoBar(chattingUser);
            } else {
                updateUserInfoBar(null);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<SimpleUser> list = GraphicEventHandler.searchFor(newValue);
            searchList.clear();
            searchList.addAll(list);
            if (newValue.isEmpty())
                contactListView.setItems(contactList);
            else
                contactListView.setItems(searchList);
        });

    }

    public void sendMessage() {
        //TODO
        String text = sendMessageTextArea.getText();
        Set<Long> targets = new LinkedHashSet<>();
        targets.add(chattingUser.getID());
        TextMessage message = new TextMessage(text, Cache.currentUser.getID(), targets, new Date());
        if (currentChat == null) {
            currentChat = new LinkedList<>();
            Cache.chats.put(chattingUser.getID(), currentChat);
        }
        Cache.chats.get(chattingUser.getID()).add(message);
        GraphicEventHandler.sendTextMessage(message);
        messages.add(message);
        if (!contactList.contains(chattingUser))
            contactList.add(chattingUser);
        sendMessageTextArea.clear();
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

    public void createGroup() {
        //TODO
    }
}
