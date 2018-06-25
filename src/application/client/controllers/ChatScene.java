package application.client.controllers;

import application.client.modules.Cache;
import application.client.modules.LogicalEventHandler;
import application.util.message.Message;
import application.util.message.TextMessage;
import application.util.user.SimpleUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class ChatScene extends GraphicController implements Initializable {
    public Menu userMenuButton;
    public TextField searchField;
    public ListView contactListView;
    @FXML
    private ListView messageLogListView;
    @FXML
    private Label nameField;
    @FXML
    private Label idField;
    @FXML
    private Label lastSeenField;
    @FXML
    private TextArea sendMessageTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GraphicController.nameField = nameField;
        GraphicController.idField = idField;
        GraphicController.lastSeenField = lastSeenField;
        GraphicController.contactListView = contactListView;
        updateUserInfoBar(null);
        ObservableList<SimpleUser> searchList = FXCollections.observableArrayList();
        contactList = FXCollections.observableArrayList();
        messages = FXCollections.observableArrayList();
        messageLogListView.setItems(messages);
        contactListView.setItems(contactList);
        searchList.clear();
        for (Long id : Cache.getChats().keySet())
            contactList.add(LogicalEventHandler.getUserInfo(id));
        //TODO
        userMenuButton.setText(Cache.getCurrentUser().getName());
        contactListView.setCellFactory(new Callback<ListView<SimpleUser>, ListCell<SimpleUser>>() {
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
                currentChat = Cache.getChat(chattingUser.getID());
                if (currentChat != null)
                    messages.addAll(Cache.getChat(chattingUser.getID()));
                updateUserInfoBar(chattingUser);
            } else {
                updateUserInfoBar(null);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<SimpleUser> list = searchFor(newValue);
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
        TextMessage message = new TextMessage(text, Cache.getCurrentUser().getID(), targets, new Date());
        Cache.addMessage(message);
        LogicalEventHandler.sendTextMessage(message);
        messages.add(message);
        if (!contactList.contains(chattingUser))
            contactList.add(chattingUser);
        sendMessageTextArea.clear();
    }

    public void openSettings() {
        //TODO
    }

    public void signOut() {
        LogicalEventHandler.signOut();
        goTo("StartUp");
    }

    public void close() {
        System.exit(0);
    }

    public void createGroup() {
        goTo("CreateGroupDialog");
    }
}
