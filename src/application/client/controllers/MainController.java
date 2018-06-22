package application.client.controllers;

import application.client.ClientMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;

import java.io.IOException;

abstract class MainController {
    static void goTo(String name) {
        try {
            ClientMain.getScene().setRoot(FXMLLoader.load(ClientMain.class.getResource("views/fxml/"+name+".fxml")));
            ClientMain.getScene().getStylesheets().add(ClientMain.class.getResource("views/stylesheets/"+name+".css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Alert getNewAlert(Alert.AlertType alertType, String text){
        Alert alert = new Alert(alertType,text);
        alert.getDialogPane().getStylesheets().add(ClientMain.class.getResource("views/stylesheets/Alert.css").toExternalForm());
        return alert;
    }
}
