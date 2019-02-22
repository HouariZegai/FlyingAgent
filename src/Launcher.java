import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/Main.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Flying Agent");
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}