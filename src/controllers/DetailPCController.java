package controllers;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.table_models.NetworkTable;
import information.*;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import models.Message;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class DetailPCController implements Initializable {

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

    // Network information
    @FXML
    private JFXTreeTableView<NetworkTable> tableNetwork;

    // Disks information
    @FXML
    private HBox boxContainerDisks;

    private JFXTreeTableColumn<NetworkTable, String> colName, colIP, colMAC;
    private AgentController mainController;
    // More infos
    @FXML
    private TextArea areaMoreInfo;

    public static Double humanReadableByteCountTwo(long bytes) {
        int unit = 1024;
        if (bytes < unit) return (double) bytes;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp - 1) + "";
        return Double.valueOf(new DecimalFormat("##.##").format(bytes / Math.pow(unit, exp)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        /* Init network info (table) */
        initNetworkTable();

        //initBoxesDisk(); // Init disk info
        //loadDataToNetworkTable();
    }

    void updateScreen(AllInformation allInformation) {
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

        data.add(new PieChart.Data("In Use", humanReadableByteCountTwo(memoryInfo.getInUseMemorySize())));
        data.add(new PieChart.Data("Free", humanReadableByteCountTwo(memoryInfo.getFreePhysicalMemory())));
        //data.add(new PieChart.Data("In Use", Utils.humanReadableByteCount(3000000012L)));
        //data.add(new PieChart.Data("Free", Utils.humanReadableByteCount(2200000012L)));


        pieMemory.setData(data);
        pieMemory.getData().forEach(d ->
                d.nameProperty().bind(Bindings.concat(d.getName(), " ", d.pieValueProperty(), " GB"))
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
            for (Disk disk : disks)
                addDiskBox(disk);
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
            data.add(new PieChart.Data("Usable", humanReadableByteCountTwo(disk.getUsableSpace())));
            data.add(new PieChart.Data("Free", humanReadableByteCountTwo(disk.getFreeSpace())));

            pieData.setData(data);
            pieData.getData().forEach(d ->
                    d.nameProperty().bind(Bindings.concat(d.getName(), " ", d.pieValueProperty(), " GB"))
            );
            boxContainerDisks.getChildren().add(parentDisk);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void onMoreInfos() {
        Message message = new Message(null, Message.ASK_REQUEST);
        try {
            mainController.putO2AObject(message, AgentController.ASYNC);
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void updateMoreInfo(String info) {
        areaMoreInfo.setText(info);
    }

    @FXML
    private void onExport() {

    }

    @FXML
    private void onClose() {
        HomeController.dialogDetailPC.close();
    }

    public void setMainAgentController(AgentController mainController) {
        if (mainController == null) System.out.println("Main Controller is Null");
        else System.out.println("Main Controller is not Null");
        this.mainController = mainController;
    }
}
