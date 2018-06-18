package application.client;

import application.client.modules.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

    private static Scene scene;

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        Network.initialize("localhost", 8080);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("views/StartUp.fxml"));
        primaryStage.setTitle("Messenger");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        //TODO Icon
        primaryStage.show();
    }
}
