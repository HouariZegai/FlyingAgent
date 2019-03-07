package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.table_models.NetworkTable;
import information.*;
import jade.util.leap.Iterator;
import jade.wrapper.AgentController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.Launcher;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScanAllController implements Initializable {

    List<AllInformation> allInformationList;

    @FXML
    private JFXListView<StackPane> listLocation;
    /* OS information */
    @FXML
    private Label lblOSName, lblOSVersion, lblOSArchi, lblOSUsername, lblOSComputerName;
    @FXML
    private ImageView iconOs;
    /* CPU information */
    @FXML
    private Label lblCPUId, lblCPUArchi, lblCPUNumCores;
    @FXML
    private ImageView iconCpu;
    /* Memory information */
    @FXML
    private PieChart pieMemory;
    /* Network information */
    @FXML
    private JFXTreeTableView<NetworkTable> tableNetwork;
    private JFXTreeTableColumn<NetworkTable, String> colName, colIP, colMAC;
    // Disks information
    @FXML
    private HBox boxContainerDisks;
    /* More infos */
    @FXML
    private JFXSpinner spinnerMoreInfo;
    @FXML
    private TextArea areaMoreInfo;

    private jade.util.leap.List locationsJade;

    private Parent mainView;

    public void updateLocation(jade.util.leap.List locations) {
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

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        initNetworkTable(); // Init network info (table)
        listLocation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            listLocation.setExpanded(true);
            listLocation.depthProperty().set(3);
            // change detail pc showing
            int selectedIndex = listLocation.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0)
                updateScreen(allInformationList.get(selectedIndex));
        });
    }

    private void updateScreen(AllInformation allInformation) {
        initOS(allInformation.getOsInformation());
        initMemoryChart(allInformation.getMemoryInformation());
        initCPU(allInformation.getCpuInformation());
        loadDataToNetworkTable(allInformation.getNetworkInformation());
        initBoxesDisk(allInformation.getDisksInformation());
    }

    /* End OS Info */

    /* Start CPU Info */

    private void initOS(OSInformation osInfo) {
        if (osInfo.getOsName().toLowerCase().contains("windows"))
            iconOs.setImage(new Image("/resources/images/os/win.png"));
        else if (osInfo.getOsName().toLowerCase().contains("linux") || osInfo.getOsName().toLowerCase().contains("unix"))
            iconOs.setImage(new Image("/resources/images/os/win.png"));
        if (osInfo.getOsName().toLowerCase().contains("mac") || osInfo.getOsName().toLowerCase().contains("apple"))
            iconOs.setImage(new Image("/resources/images/os/apple.png"));

        lblOSName.setText(osInfo.getOsName());
        lblOSVersion.setText(osInfo.getOsVersion());
        lblOSArchi.setText(osInfo.getOsArchitecture());
        lblOSUsername.setText(osInfo.getUserName());
        lblOSComputerName.setText(osInfo.getComputerName());
    }

    /* End CPU Info */

    /* Start Memory info */

    private void initCPU(CPUInformation cpuInfo) {
        if (cpuInfo.getProcessorId().toLowerCase().contains("intel"))
            iconCpu.setImage(new Image("/resources/images/cpu/intel.png"));
        else if (cpuInfo.getProcessorId().toLowerCase().contains("amd"))
            iconCpu.setImage(new Image("/resources/images/cpu/amd.png"));

        lblCPUId.setText(cpuInfo.getProcessorId());
        lblCPUArchi.setText(cpuInfo.getProcessorArchitecture());
        lblCPUNumCores.setText(cpuInfo.getNumberCores());

    }

    /* End Memory info */

    /* Start Table Network Info */

    private void initMemoryChart(MemoryInformation memoryInfo) {


        pieMemory.setTitle("Total size: " + Utils.humanReadableByteCount(memoryInfo.getPhysicalMemorySize()));
        //pieMemory.setTitle("Total size: " + Utils.humanReadableByteCount(5500000012L) + "GB");

        // Data of pie chart
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        data.add(new PieChart.Data("In Use " + Utils.humanReadableByteCount(memoryInfo.getInUseMemorySize()), Utils.humanReadableByteCountNumber(memoryInfo.getInUseMemorySize())));
        data.add(new PieChart.Data("Free " + Utils.humanReadableByteCount(memoryInfo.getFreePhysicalMemory()), Utils.humanReadableByteCountNumber(memoryInfo.getFreePhysicalMemory())));
        //data.add(new PieChart.Data("In Use", Utils.humanReadableByteCount(3000000012L)));
        //data.add(new PieChart.Data("Free", Utils.humanReadableByteCount(2200000012L)));


        pieMemory.setData(data);

        pieMemory.getData().forEach(d -> {
                    d.nameProperty().bind(Bindings.concat(d.getName()));
                }
        );
    }

    private void initNetworkTable() {
        colName = new JFXTreeTableColumn<>("Name");
        colName.setPrefWidth(400d);
        colName.setCellValueFactory((TreeTableColumn.CellDataFeatures<NetworkTable, String> param) -> param.getValue().getValue().nameProperty());

        colIP = new JFXTreeTableColumn<>("IP");
        colIP.setPrefWidth(207d);
        colIP.setCellValueFactory((TreeTableColumn.CellDataFeatures<NetworkTable, String> param) -> param.getValue().getValue().ipProperty());

        colMAC = new JFXTreeTableColumn<>("MAC");
        colMAC.setPrefWidth(207d);
        colMAC.setCellValueFactory((TreeTableColumn.CellDataFeatures<NetworkTable, String> param) -> param.getValue().getValue().macProperty());

        tableNetwork.getColumns().addAll(colName, colIP, colMAC);
        tableNetwork.setShowRoot(false);
    }

    /* End Table Network Info */

    /* Start disks info */

    private void loadDataToNetworkTable(NetworkInformation networkInformation) {
        ObservableList<NetworkTable> networkTableData = FXCollections.observableArrayList();

        /* This data add below just for testing */
        for (Network network : networkInformation.getNetworkList()) {
            networkTableData.add(new NetworkTable(network.getName(), network.getIpAddress(), network.getMacAddress()));
        }

        TreeItem treeItem = new RecursiveTreeItem<>(networkTableData, RecursiveTreeObject::getChildren);
        try {
            tableNetwork.setRoot(treeItem);
        } catch (Exception e) {
            System.err.println("Exception in tree item of network table !");
        }
    }

    private void initBoxesDisk(DisksInformation disksInformation) {
        boxContainerDisks.getChildren().clear();
        List<Disk> disks = disksInformation.getDisks();
        if (disks != null) {
            for (Disk disk : disks) {
                if (disk.getTotalSpace() != 0L)
                    addDiskBox(disk);
            }
        }

        /* This data just for testing */
//        Disk disk = new Disk("C:\\", 96779665408L, 15609087488L, 95210577920L);
//        addDiskBox(disk);
//        addDiskBox(disk);
//        addDiskBox(disk);
    }

    /* End disks info */

    private void addDiskBox(Disk disk) {
        try {
            Parent parentDisk = FXMLLoader.load(getClass().getResource("/resources/views/models/DiskInfo.fxml"));
            //Label lblName = (Label) parentDisk.lookup("#lblName");
            Label lblTotalSpace = (Label) parentDisk.lookup("#lblTotalSpace");
            PieChart pieData = (PieChart) parentDisk.lookup("#pieData");

            pieData.setTitle(disk.getName());
            lblTotalSpace.setText(Utils.humanReadableByteCount(disk.getTotalSpace()));

            // Data of pie chart
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            data.add(new PieChart.Data("Usable", Utils.humanReadableByteCountNumber(disk.getUsableSpace())));
            data.add(new PieChart.Data("Free", Utils.humanReadableByteCountNumber(disk.getFreeSpace())));

            pieData.setData(data);
            pieData.getData().forEach(d ->
                    d.nameProperty().bind(Bindings.concat(d.getName(), " ", d.pieValueProperty(), " GB"))
            );
            boxContainerDisks.getChildren().add(parentDisk);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void setMainAgentController(AgentController mainController) {
    }

    public void updateInformations(List<AllInformation> all) {
        this.allInformationList = all;
    }

    @FXML // back to main (back to select scan type)
    private void onBack() {
        // Load main.Main View
        try {
            mainView = FXMLLoader.load(getClass().getResource("/resources/views/Main.fxml"));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        Launcher.stage.setScene(new Scene(mainView));
        Launcher.centerOnScreen();
    }
}
