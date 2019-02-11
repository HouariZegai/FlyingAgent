package controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private StackPane root;

    @FXML
    private JFXListView<Label> listLocation;

    public static JFXDialog dialogDetailPC;

    private JFXSnackbar toastMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        toastMsg = new JFXSnackbar(root);

        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/resources/views/DetailPC.fxml"));
        DetailPCController detailPCController = detailsLoader.getController();

        try {
            VBox detailsPCPane = detailsLoader.load();
            dialogDetailPC = new JFXDialog(root, detailsPCPane, JFXDialog.DialogTransition.CENTER);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void updateLocation(List<String> locations) {
        listLocation.getItems().clear();
        if(locations != null)
            for(String location : locations) {
                listLocation.getItems().add(new Label(location));
            }
    }

    @FXML
    private void onRefresh() {
        if(listLocation.isExpanded()) {
           listLocation.setExpanded(false);
            listLocation.depthProperty().set(0);
        } else {
            listLocation.setExpanded(true);
           listLocation.depthProperty().set(1);
        }
    }

    @FXML
    private void onDetail() {
        if(listLocation.getSelectionModel().getSelectedItem() == null) {
            toastMsg.show("Please Select Container of view detail !", 3000);
            return;
        }

        dialogDetailPC.show();
    }
}
