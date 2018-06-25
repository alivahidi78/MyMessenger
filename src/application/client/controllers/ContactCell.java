package application.client.controllers;

import application.util.user.SimpleUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ContactCell {
    public HBox hBox;
    public ImageView image;
    public Label username;
    public Label name;
    public Label msgNum;

    public ContactCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../views/fxml/ContactCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(SimpleUser user) {
        if (user.getImage() != null)
            image.setImage(user.getImage());
        else
            image.setImage(new Image(getClass().getResource("../views/images/default_user.gif").toExternalForm()));
        username.setText("@" + user.getUsername());
        name.setText(user.getName());
    }

    public void addUnreadLabel(int n){
        if(n<0)
            msgNum.setText("");
        else
            msgNum.setText(String.valueOf(Integer.valueOf(msgNum.getText())+n));
    }

    public HBox getBox() {
        return hBox;
    }
}
