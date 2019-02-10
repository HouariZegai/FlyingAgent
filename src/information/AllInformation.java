package information;

public class AllInformation extends Information {

    private static AllInformation sInstance;
    private NetworkInformation networkInformation;
    private CPUInformation cpuInformation;
    private DisksInformation disksInformation;
    private MemoryInformation memoryInformation;
    private OSInformation osInformation;


    private AllInformation() {
        networkInformation = new NetworkInformation();
        cpuInformation = new CPUInformation();
        disksInformation = new DisksInformation();
        memoryInformation = new MemoryInformation();
        osInformation = new OSInformation();
    }

    public static AllInformation getsInstance() {
        return sInstance;
    }

    public static AllInformation getInstance() {
        if (sInstance == null) sInstance = new AllInformation();
        return sInstance;
    }

    public NetworkInformation getNetworkInformation() {
        return networkInformation;
    }

    public CPUInformation getCpuInformation() {
        return cpuInformation;
    }

    public DisksInformation getDisksInformation() {
        return disksInformation;
    }

    public MemoryInformation getMemoryInformation() {
        return memoryInformation;
    }

    public OSInformation getOsInformation() {
        return osInformation;
    }
}
