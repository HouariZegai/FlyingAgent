package information;

public class Network {

    private String ipAddress;
    private String macAddress;
    private String name;

    Network(String ipAddress, String macAddress, String name) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Network{" +
                "ipAddress='" + ipAddress + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }
}
