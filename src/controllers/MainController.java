package controllers;

import agents.MainAgent;
import agents.MobileAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Message;
import utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static AgentContainer mc;

    private Parent scanEachView, scanAllView;

    private AgentController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader scanEachLoader = new FXMLLoader(getClass().getResource("/resources/views/ScanEach.fxml"));
            scanEachView = scanEachLoader.load();
            FXMLLoader scanAllLoader = new FXMLLoader(getClass().getResource("/resources/views/ScanAll.fxml"));
            scanAllView = scanAllLoader.load();
            initAgent(scanEachLoader, scanAllLoader);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void onScanAll(MouseEvent event) {
        Message message = new Message(null, Message.SCAN_ALL_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(scanAllView));
    }

    @FXML
    private void onScanEach(MouseEvent event) {
        Message message = new Message(null, Message.REFRESH_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(scanEachView));
    }

    private void initAgent(FXMLLoader scanEachLoader, FXMLLoader loader) {
        // Get a hold on JADE runtime
        Runtime rt = Runtime.instance();

        // Exit the JVM when there are no more containers around
        rt.setCloseVM(true);

        // Launch a complete platform on the 8888 port
        // create a default Profile
        Profile pMain = new ProfileImpl(Constants.MAIN_CONTAINER_HOST_IP, Constants.MAIN_CONTAINER_PORT, Constants.PLATFORM_ID);
        mc = rt.createMainContainer(pMain);

        try {
            AgentController receiverAgent = mc.createNewAgent(MainAgent.NAME, MainAgent.class.getName(), new Object[]{});
            receiverAgent.start();
            mainController = receiverAgent;
            AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            //rma.start();
            AgentController mobileAgent = mc.createNewAgent("Service-Agent", MobileAgent.class.getName(), new Object[]{});
            mobileAgent.start();
            if (scanEachLoader.getController() instanceof ScanEachController) {
                ScanEachController scanEachController = scanEachLoader.getController();
                scanEachController.setMainAgentController(receiverAgent);
                MainAgent.setScanEachController(scanEachController);
            }
            if (loader.getController() instanceof ScanAllController) {
                ScanAllController scanAllController = loader.getController();
                scanAllController.setMainAgentController(receiverAgent);
                MainAgent.setScanAllController(scanAllController);
            }
        } catch (StaleProxyException spe) {
            spe.printStackTrace();
        }
    }
}
