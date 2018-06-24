package application.client.controllers;

import application.client.modules.GraphicEventHandler;
import application.util.answer.Answer;
import application.util.answer.AnswerType;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUp extends MainController implements Initializable {
    public TextField nameField;
    public TextField passwordField;
    public TextField usernameField;
    public ImageView imageView;
    //TODO send image

    public void signUp() {
        if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            getNewAlert(Alert.AlertType.ERROR, "Please fill all the fields.").show();
            return;
        }
        Answer answer = GraphicEventHandler.requestSignUp(nameField.getText(), usernameField.getText(), passwordField.getText(),
                imageView.getImage());
        switch (answer.type) {
            case SIGN_UP_ACCEPTED:
                getNewAlert(Alert.AlertType.INFORMATION, "Signed up successfully!").show();
                Answer answer2 = GraphicEventHandler.requestSignIn(usernameField.getText(), passwordField.getText());
                if (answer2.type == AnswerType.CONNECTION_FAILED) {
                    getNewAlert(Alert.AlertType.ERROR, "Connection refused!").show();
                    break;
                }
                goTo("ChatScene");
                break;
            case SIGN_UP_DENIED:
                getNewAlert(Alert.AlertType.ERROR, "Failed to sign up!").show();
                break;
            case CONNECTION_FAILED:
                getNewAlert(Alert.AlertType.ERROR, "Connection refused!").show();
                break;
        }

    }

    public void goToSignIn() {
        goTo("StartUp");
    }

    public void chooseImage() {
        Image image = GraphicEventHandler.choosePicture();
        imageView.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
