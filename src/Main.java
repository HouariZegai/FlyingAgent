import javafx.application.Application;
import information.AllInformation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void mains(String[] args) throws IOException {
        AllInformation information = new AllInformation();
        System.out.println(information.toString());
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/views/Main.fxml"));

        stage.setScene(new Scene(root));
        stage.show();
    }
}
