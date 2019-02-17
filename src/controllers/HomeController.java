package controllers;

import agents.MainAgent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSpinner;
import information.AllInformation;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.Message;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public static JFXDialog dialogDetailPC;
    @FXML
    private StackPane root;
    private AgentController mainController;

    @FXML
    private JFXListView<StackPane> listLocation;

    @FXML
    private JFXSpinner spinnerDetails;

    private JFXSnackbar toastMsg;

    private DetailPCController detailPCController;
    private List locationsJade;

    private java.util.List<String> stringLocationList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toastMsg = new JFXSnackbar(root);

        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/resources/views/DetailPC.fxml"));
        try {
            VBox detailsPCPane = detailsLoader.load();
            dialogDetailPC = new JFXDialog(root, detailsPCPane, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        detailPCController = detailsLoader.getController();
        MainAgent.setDetailController(detailPCController);
        dialogDetailPC.setOnDialogClosed(e -> {
            listLocation.getSelectionModel().clearSelection();
        });
        listLocation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listLocation.setExpanded(true);
            listLocation.depthProperty().set(3);
        });
    }

    public void updateLocation(List locations) {
        this.locationsJade = locations;
        Iterator ite = locationsJade.iterator();

        listLocation.getItems().clear();
        while (ite.hasNext()) {
            StackPane stackPane = new StackPane();

            Label lbl = new Label(ite.next().toString());
            ImageView pcIcon = new ImageView(new Image("/resources/images/pc.png"));
            stackPane.getChildren().addAll(lbl, pcIcon);
            stackPane.setAlignment(lbl, Pos.CENTER_LEFT);
            stackPane.setAlignment(pcIcon, Pos.CENTER_RIGHT);
            listLocation.getItems().add(stackPane);
        }

    }

    @FXML
    private void onRefresh() {
        listLocation.getSelectionModel().clearSelection();
        listLocation.setExpanded(false);
        listLocation.depthProperty().set(1);

        Message message = new Message(null, Message.REFRESH_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onScanAll() {

        Message message = new Message(null, Message.SCAN_ALL_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAsk() {
        Message message = new Message(null, Message.ASK_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDetail() {
        if (listLocation.getSelectionModel().getSelectedItem() == null) {
            toastMsg.show("Please Select Container of view detail !", 3000);
            return;
        }

        spinnerDetails.setVisible(true);

        Map<String, Object> map = new HashMap<>();
        map.put(Message.KEY_LOCATION, locationsJade.get(listLocation.getSelectionModel().getSelectedIndex()));
        Message message = new Message(map, Message.MOVE_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void setMainAgentController(AgentController mainController) {
        this.mainController = mainController;
        detailPCController.setMainAgentController(mainController);
    }

    public void updateDetail(AllInformation all) {
        detailPCController.updateScreen(all);
        spinnerDetails.setVisible(false);
        dialogDetailPC.show();
    }
}
