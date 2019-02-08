import information.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(new OperatingSystemInfo());
        System.out.println(new NetworkInfo());
        System.out.println(new MemoryInfo());
        System.out.println(new CpuInfo());
        System.out.println(new DisksInfo());
    }
}
