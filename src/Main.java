import javax.swing.filechooser.FileSystemView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

public class Main {

    public static void mainOne(String[] args) {

        InetAddress ip;
        try {

            Map<String, String> map = System.getenv();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println("Map info !" + entry.getKey() + "/" + entry.getValue());
            }
            ip = InetAddress.getLocalHost();
            System.out.println("Current host name : " + ip.getHostName());
            System.out.println("Current IP address : " + ip.getHostAddress());
            String nameOS = System.getProperty("os.name");
            System.out.println("Operating system Name=>" + nameOS);
            String osType = System.getProperty("os.arch");
            System.out.println("Operating system type =>" + osType);
            String osVersion = System.getProperty("os.version");
            System.out.println("Operating system version =>" + osVersion);

            System.out.println(System.getenv("PROCESSOR_IDENTIFIER"));
            System.out.println(System.getenv("PROCESSOR_ARCHITECTURE"));
            System.out.println(System.getenv("PROCESSOR_ARCHITECTURE6432"));
            System.out.println(System.getenv("NUMBER_OF_PROCESSORS"));
            /* Total number of processors or cores available to the JVM */
            System.out.println("Available processors (cores): " +
                    Runtime.getRuntime().availableProcessors());

            /* Total amount of free memory available to the JVM */
            System.out.println("Free memory (bytes): " +
                    Runtime.getRuntime().freeMemory());

            /* This will return Long.MAX_VALUE if there is no preset limit */
            long maxMemory = Runtime.getRuntime().maxMemory();
            /* Maximum amount of memory the JVM will attempt to use */
            System.out.println("Maximum memory (bytes): " +
                    (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

            /* Total memory currently in use by the JVM */
            System.out.println("Total memory (bytes): " +
                    Runtime.getRuntime().totalMemory());


            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            System.out.print("Current MAC address : ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println(sb.toString());

            System.out.println("Display Name : " + network.getName());

            File[] roots = File.listRoots();

            FileSystemView fsv = FileSystemView.getFileSystemView();


            /* For each filesystem root, print some info */
            for (File root : roots) {
                System.out.println("File system root: " + root.getAbsolutePath());
                System.out.println("Total space (bytes): " + root.getTotalSpace());
                System.out.println("Is drive: " + fsv.isDrive(root));
                System.out.println("Is floppy: " + fsv.isFloppyDrive(root));
                System.out.println("Free space (bytes): " + root.getFreeSpace());
                System.out.println("Usable space (bytes): " + root.getUsableSpace());
            }


            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("systeminfo");
            BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = systemInformationReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }

            System.out.println("Fina One : " + stringBuilder.toString().trim());


        } catch (UnknownHostException | SocketException e) {

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String args[]) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            displayInterfaceInformation(netint);
        }
    }

    private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.printf("Display name: %s%n", netint.getDisplayName());
        System.out.printf("Name: %s%n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("InetAddress: %s%n", inetAddress);
        }

        System.out.printf("Parent: %s%n", netint.getParent());
        System.out.printf("Up? %s%n", netint.isUp());
        System.out.printf("Loopback? %s%n", netint.isLoopback());
        System.out.printf("PointToPoint? %s%n", netint.isPointToPoint());
        System.out.printf("Supports multicast? %s%n", netint.isVirtual());
        System.out.printf("Virtual? %s%n", netint.isVirtual());
        System.out.printf("Hardware address: %s%n", Arrays.toString(netint.getHardwareAddress()));
        System.out.printf("MTU: %s%n", netint.getMTU());

        List<InterfaceAddress> interfaceAddresses = netint.getInterfaceAddresses();
        for (InterfaceAddress addr : interfaceAddresses) {
            System.out.printf("InterfaceAddress: %s%n", addr.getAddress());
        }
        System.out.printf("%n");
        Enumeration<NetworkInterface> subInterfaces = netint.getSubInterfaces();
        for (NetworkInterface networkInterface : Collections.list(subInterfaces)) {
            System.out.printf("%nSubInterface%n");
            displayInterfaceInformation(networkInterface);
        }
        System.out.printf("%n");
    }

}
