package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void onScanAll(MouseEvent e) {
        try {
            Parent scanAllView = FXMLLoader.load(getClass().getResource("/resources/views/ScanEach.fxml"));
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).setScene(new Scene(scanAllView));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void onScanEach(MouseEvent e) {
        try {
            Parent scanEachView = FXMLLoader.load(getClass().getResource("/resources/views/ScanEach.fxml"));
            ((Stage) ((Node) e.getSource()).getScene().getWindow()).setScene(new Scene(scanEachView));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
