package models.information;

import java.io.Serializable;

public class AllInformation extends Information implements Serializable {

    private static AllInformation sInstance;
    private NetworkInformation networkInformation;
    private CPUInformation cpuInformation;
    private DisksInformation disksInformation;
    private MemoryInformation memoryInformation;
    private OSInformation osInformation;


    public AllInformation() {
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

    @Override
    public String toString() {
        return "AllInformation{" +
                "networkInformation=" + networkInformation +
                ", cpuInformation=" + cpuInformation +
                ", disksInformation=" + disksInformation +
                ", memoryInformation=" + memoryInformation +
                ", osInformation=" + osInformation +
                '}';
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
