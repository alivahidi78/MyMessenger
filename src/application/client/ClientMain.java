package application.client;

import application.client.modules.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
        scene = new Scene(FXMLLoader.load(getClass().getResource("views/StartUp.fxml")));
        scene.getStylesheets().add(getClass().getResource("views/stylesheets/StartUp.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Messenger");
        //TODO Icon
        primaryStage.show();
    }
}
