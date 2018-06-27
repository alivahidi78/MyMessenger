package application.client.controllers;

import application.client.modules.Cache;
import application.client.modules.LogicalEventHandler;
import application.util.message.FileMessage;
import application.util.message.Message;
import application.util.message.TextMessage;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ChatScene extends GraphicController implements Initializable {
    public Menu userMenuButton;
    public ListView<Message> messageLogListView;
    public Label nameField;
    public Label idField;
    public Label lastSeenField;
    public TextArea sendMessageTextArea;
    public VBox contactListBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GraphicController.name = new SimpleStringProperty();
        GraphicController.id = new SimpleStringProperty();
        GraphicController.lastSeen = new SimpleStringProperty();
        nameField.textProperty().bind(name);
        idField.textProperty().bind(id);
        lastSeenField.textProperty().bind(lastSeen);
        updateUserInfoBar(null);
        messages = FXCollections.observableArrayList();
        messageLogListView.setItems(messages);
        userMenuButton.setText(Cache.getCurrentUser().getName());
        messageLogListView.setCellFactory(new Callback<>() {
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
        contactListBox.getChildren().add(getContactList());
        selectedUser.addListener((observable, oldValue, newValue) -> {
            chattingUser = newValue;
            messages.clear();
            if (chattingUser != null) {
                currentChat = Cache.getChat(chattingUser.getID());
                if (currentChat != null) {
                    messages.addAll(Cache.getChat(chattingUser.getID()));
                }
                updateUserInfoBar(chattingUser);
            } else {
                updateUserInfoBar(null);
            }
        });
    }

    public void sendMessage() {
        //TODO
        String text = sendMessageTextArea.getText();
        long groupID = -1;
        if (chattingUser.isGroup())
            groupID = chattingUser.getID();
        TextMessage message = new TextMessage(text, Cache.getCurrentUser().getID(), groupID, chattingUser.getID(), new Date());
        Cache.addMessage(message);
        LogicalEventHandler.sendMessage(message);
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

    public void sendFile() {
        DoubleProperty property = new SimpleDoubleProperty();
        File file = chooseFile();
        if (file == null) {
            return;
        }
        long groupID = -1;
        if (chattingUser.isGroup())
            groupID = chattingUser.getID();
        FileMessage fileMessage = new FileMessage(Cache.getCurrentUser().getID(), groupID,
                chattingUser.getID(), new Date(), file.getName());
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                LogicalEventHandler.sendFileToServer(fileMessage, file, property);
                return null;
            }
        };
        Cache.addMessage(fileMessage);
        messages.add(fileMessage);
        fileMessage.progress.bind(property);
        Thread thread = new Thread(t);
        thread.setDaemon(true);
        thread.start();
    }
}
