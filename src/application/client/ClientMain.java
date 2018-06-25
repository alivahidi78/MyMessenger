package application.client;

import application.client.modules.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application {

    private static Scene scene;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        Network.initialize("localhost", 8085);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        scene = new Scene(FXMLLoader.load(getClass().getResource("views/fxml/StartUp.fxml")));
        scene.getStylesheets().add(getClass().getResource("views/stylesheets/StartUp.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("My Messenger");
        primaryStage.getIcons().add(new Image(getClass().getResource("views/images/icon.png").toExternalForm()));
        primaryStage.show();
    }
}
