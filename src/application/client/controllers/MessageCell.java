package application.client.controllers;

import application.client.modules.LogicalEventHandler;
import application.util.message.FileMessage;
import application.util.message.Message;
import application.util.message.MessageType;
import application.util.message.TextMessage;
import application.util.user.Info;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class MessageCell {
    public Label name;
    public Text text;
    public Label date;
    public ImageView usrImg;
    public HBox hBox;
    public ProgressIndicator progressIndicator;

    public MessageCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/fxml/MessageCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Message message) {
        Info user = LogicalEventHandler.getUserInfo(message.sender);
        progressIndicator.progressProperty();
        if (user.getImage() != null)
            usrImg.setImage(user.getImage());
        else
            usrImg.setImage(new Image(getClass().getResource("../views/images/default_user.png").toExternalForm()));
        name.setText(user.getName());
        if (message.type == MessageType.TEXT) {
            text.setText(((TextMessage) message).getText());
            progressIndicator.setVisible(false);
        }
        if (message.type == MessageType.FILE) {
            progressIndicator.setVisible(true);
            progressIndicator.progressProperty().bind(((FileMessage)message).progress);
            text.setText(((FileMessage)message).name);
        }
        date.setText(message.date.toString());
    }

    public HBox getBox() {
        return hBox;
    }
}
