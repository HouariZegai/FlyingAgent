package information;

public class OperatingSystemInfo {

    private String osName;
    private String osVersion;
    private String osArchitecture;
    private String userName;
    private String computerName;

    public OperatingSystemInfo() {
        osName = System.getProperty("os.name");
        osVersion = System.getProperty("os.version");
        osArchitecture = System.getProperty("os.arch");
        userName = System.getProperty("user.name");
        computerName = System.getenv("COMPUTERNAME");
    }

    @Override
    public String toString() {
        return "OperatingSystemInfo{" +
                "osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", osArchitecture='" + osArchitecture + '\'' +
                ", userName='" + userName + '\'' +
                ", computerName='" + computerName + '\'' +
                '}';
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getOsArchitecture() {
        return osArchitecture;
    }

    public String getUserName() {
        return userName;
    }

    public String getComputerName() {
        return computerName;
    }
}
