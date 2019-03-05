package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    // Principal Stage (current stage)
    public static Stage stage;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/Main.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Flying Agent");
            Launcher.stage = stage;
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void centerOnScreen() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Launcher.stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        Launcher.stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }

}