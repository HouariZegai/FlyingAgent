package controllers;

import agents.AgentsContainerInstance;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import main.Launcher;
import models.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    private AgentController mainController;
    private Parent scanEachView, scanAllView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader scanEachLoader = new FXMLLoader(getClass().getResource("/resources/views/ScanEach.fxml"));
            scanEachView = scanEachLoader.load();
            FXMLLoader scanAllLoader = new FXMLLoader(getClass().getResource("/resources/views/ScanAll.fxml"));
            scanAllView = scanAllLoader.load();
            mainController = AgentsContainerInstance.getInstance(scanEachLoader, scanAllLoader);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void onScanAll() {
        sendMessage(Message.REFRESH_REQUEST);
        sendMessage(Message.SCAN_ALL_REQUEST);
        Launcher.stage.setScene(new Scene(scanAllView));
        Launcher.centerOnScreen();
    }

    @FXML
    private void onScanEach() {
        sendMessage(Message.REFRESH_REQUEST);
        Launcher.stage.setScene(new Scene(scanEachView));
        Launcher.centerOnScreen();
    }

    private void sendMessage(int refreshRequest) {
        Message message = new Message(null, refreshRequest);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

}
