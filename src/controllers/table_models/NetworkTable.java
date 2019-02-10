package controllers.table_models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NetworkTable extends RecursiveTreeObject<NetworkTable> {
    private StringProperty name;
    private StringProperty ip;
    private StringProperty mac;

    public NetworkTable() {
        name = new SimpleStringProperty();
        ip = new SimpleStringProperty();
        mac = new SimpleStringProperty();
    }

    public NetworkTable(String name, String ip, String mac) {
        this();
        this.name.set(name);
        this.ip.set(ip);
        this.mac.set(mac);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getIp() {
        return ip.get();
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public String getMac() {
        return mac.get();
    }

    public void setMac(String mac) {
        this.mac.set(mac);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public StringProperty macProperty() {
        return mac;
    }
}
