import information.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(new CPUInformation().toString());
        System.out.println(new DisksInformation().toString());
        System.out.println(new MemoryInformation().toString());
        System.out.println(new NetworkInformation().toString());
        System.out.println(new OSInformation().toString());
    }
}
