package application.client.controllers;

import application.client.modules.GraphicEventHandler;
import application.util.answer.Answer;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SignUp extends MainController {
    public TextField nameField;
    public TextField passwordField;
    public TextField usernameField;

    public void signUp() {
        if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields.").show();
            return;
        }
        Answer answer = GraphicEventHandler.requestSignUp(nameField.getText(), usernameField.getText(), passwordField.getText());
        switch (answer.type) {
            case SIGN_UP_ACCEPTED:
                new Alert(Alert.AlertType.INFORMATION, "Signed up successfully!").show();
                break;
            case SIGN_UP_DENIED:
                new Alert(Alert.AlertType.ERROR, "Failed to sign up!").show();
                break;
        }

    }

    public void goToSignIn() {
        goTo("StartUp");
    }
}
