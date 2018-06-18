package application.client.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatScene extends MainController implements Initializable {
    public TextArea sendMessageTextArea;
    public ListView messageLog;
    public ListView contactList;

    public void sendMessage() {
        //TODO
//        sendMessageTextArea.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
}
