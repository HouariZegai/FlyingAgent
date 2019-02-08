package information;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class NetworkInfo {

    private final static String IPV4_REGEX = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
    private List<Network> networkList;

    public NetworkInfo() {
        networkList = new ArrayList<>();
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration ee = networkInterface.getInetAddresses();

                while (ee.hasMoreElements()) {
                    InetAddress ip = (InetAddress) ee.nextElement();
                    Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
                    // if it is IPv4 Address
                    if (IPV4_PATTERN.matcher(ip.getHostAddress()).matches()) {

                        NetworkInterface network = NetworkInterface.getByInetAddress(ip);

                        byte[] mac = network.getHardwareAddress();
                        String macAddress;
                        if (mac != null) {
                            StringBuilder macBuilder = new StringBuilder();
                            for (int i = 0; i < mac.length; i++) {
                                macBuilder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                            }
                            macAddress = macBuilder.toString();
                        } else {
                            macAddress = "Undefined";
                        }

                        String ipAddress = ip.getHostAddress();
                        String name = networkInterface.getDisplayName();
                        networkList.add(new Network(ipAddress, macAddress, name));
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public List<Network> getNetworkList() {
        return networkList;
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "networkList=" + networkList +
                '}';
    }
}
