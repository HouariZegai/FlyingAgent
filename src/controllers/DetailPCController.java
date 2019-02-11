package controllers;

import java.net.URL;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.table_models.NetworkTable;
import information.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

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

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        initOS(); // Init OS info
        initCPU(); // Init CPU info
        initMemoryChart(); // Init memory info

        /* Init network info (table) */
        initNetworkTable();
        loadDataToNetworkTable();

        initBoxesDisk(); // Init disk info
    }

    /* Start OS Info */

    private void initOS() {
//        OSInformation osInfo = new OSInformation();
//        lblOSName.setText(osInfo.getOsName());
//        lblOSVersion.setText(osInfo.getOsVersion());
//        lblOSArchi.setText(osInfo.getOsArchitecture());
//        lblOSUsername.setText(osInfo.getUserName());
//        lblOSComputerName.setText(osInfo.getComputerName());
        /* Just for testing */
        iconOs.setImage(new Image("/resources/images/os/linux.png"));
        lblOSName.setText("Linux");
        lblOSVersion.setText("16.04");
        lblOSArchi.setText("32bit");
        lblOSUsername.setText("Houari");
        lblOSComputerName.setText("DELL-Houar");

    }

    /* End OS Info */

    /* Start CPU Info */

    private void initCPU() {
//        CPUInformation cpuInfo = new CPUInformation();
//        lblCPUId.setText(cpuInfo.getProcessorId());
//        lblCPUArchi.setText(cpuInfo.getProcessorArchitecture());
//        lblCPUNumCores.setText(cpuInfo.getNumberCores());

        /* Just for testing */
        iconCpu.setImage(new Image("/resources/images/cpu/intel.png"));
        lblCPUId.setText("Intel i3-4004U 2.16GHZ");
        lblCPUArchi.setText("64bit");
        lblCPUNumCores.setText("2");

    }

    /* End CPU Info */

    /* Start Memory info */

    private void initMemoryChart() {
        //MemoryInformation memoryInfo = new MemoryInformation();

        //pieMemory.setText(byteToGB(memoryInfo.getPhysicalMemorySize()) + "GB");
        pieMemory.setTitle("Total size: " + byteToGB(5500000012L) + "GB");

        // Data of pie chart
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

//        data.add(new PieChart.Data("In Use", byteToGB(memoryInfo.getInUseMemorySize())));
//        data.add(new PieChart.Data("Free", byteToGB(memoryInfo.getFreePhysicalMemory())));
        data.add(new PieChart.Data("In Use", byteToGB(3000000012L)));
        data.add(new PieChart.Data("Free", byteToGB(2200000012L)));


        pieMemory.setData(data);
        pieMemory.getData().forEach(d ->
                d.nameProperty().bind(Bindings.concat(d.getName(), " ", d.pieValueProperty(), " GB"))
        );
    }

    /* End Memory info */

    /* Start Table Network Info */

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

    private void loadDataToNetworkTable() {
        ObservableList<NetworkTable> networkTableData = FXCollections.observableArrayList();

        /* This data add below just for testing */
        networkTableData.add(new NetworkTable("Software Loopback Interface 1", "127.0.0.1", "Undefined"));
        networkTableData.add(new NetworkTable("Realtek PCIe GBE Family Controller", "192.168.100.135", "BC-5F-F4-9D-27-57"));

        TreeItem treeItem = new RecursiveTreeItem<>(networkTableData, RecursiveTreeObject::getChildren);
        try {
            tableNetwork.setRoot(treeItem);
        } catch (Exception e) {
            System.err.println("Exception in tree item of network table !");
        }
    }

    /* End Table Network Info */

    /* Start disks info */

    private void initBoxesDisk() {
        boxContainerDisks.getChildren().clear();
//        List<Disk> disks = new DisksInformation().getDisks();
//        if(disks != null) {
//            for(Disk disk : disks)
//                addDiskBox(disk);
//        }

        /* This data just for testing */
//        Disk disk = new Disk("C:\\", 96779665408L, 15609087488L, 95210577920L);
//        addDiskBox(disk);
//        addDiskBox(disk);
//        addDiskBox(disk);
    }

//    private void addDiskBox(Disk disk) {
//        try {
//            Parent parentDisk = FXMLLoader.load(getClass().getResource("/resources/views/models/DiskInfo.fxml"));
//            //Label lblName = (Label) parentDisk.lookup("#lblName");
//            Label lblTotalSpace = (Label) parentDisk.lookup("#lblTotalSpace");
//            PieChart pieData = (PieChart) parentDisk.lookup("#pieData");
//
//            pieData.setTitle(disk.getName());
//            lblTotalSpace.setText(byteToGB(disk.getTotalSpace()) + "GB");
//
//            // Data of pie chart
//            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
//            data.add(new PieChart.Data("Usable", byteToGB(disk.getUsableSpace())));
//            data.add(new PieChart.Data("Free", byteToGB(disk.getFreeSpace())));
//
//            pieData.setData(data);
//            pieData.getData().forEach(d ->
//                    d.nameProperty().bind(Bindings.concat(d.getName(), " ", d.pieValueProperty(), " GB"))
//            );
//            boxContainerDisks.getChildren().add(parentDisk);
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }

    /* End disks info */

    private double byteToGB(long byteSize) {
        return Double.valueOf(new DecimalFormat("##.##").format(byteSize / 1e9));
    }

    @FXML
    private void onClose() {
        HomeController.dialogDetailPC.close();
    }

}
