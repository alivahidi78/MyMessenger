package application.client.controllers;

import application.client.ClientMain;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

abstract class MainController {
    static void goTo(String name) {
        try {
            ClientMain.getScene().setRoot(FXMLLoader.load(ClientMain.class.getResource("views/"+name+".fxml")));
            ClientMain.getScene().getStylesheets().add(ClientMain.class.getResource("views/stylesheets/"+name+".css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO couldn't load
        }
    }
}
