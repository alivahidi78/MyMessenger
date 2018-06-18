package application.client.controllers;

import application.client.modules.GraphicEventHandler;
import application.util.answer.Answer;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class StartUp extends MainController {
    public TextField usernameField;
    public PasswordField passwordField;

    public void signIn() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields.").show();
            return;
        }
        Answer answer = GraphicEventHandler.requestSignIn(usernameField.getText(), passwordField.getText());
        switch (answer.type) {
            case SIGN_IN_ACCEPTED:
                new Alert(Alert.AlertType.INFORMATION, "Signed in successfully!").show();
                goTo("ChatScene");
                break;
            case SIGN_IN_DENIED:
                new Alert(Alert.AlertType.ERROR, "Wrong username and/or password!").show();
                break;
            case CONNECTION_FAILED:
                new Alert(Alert.AlertType.ERROR, "Connection refused!").show();
                break;
        }
    }

    public void loadSignUpScene() {
        goTo("SignUp");
    }
}
