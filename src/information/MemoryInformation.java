package information;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

import static information.Utils.humanReadableByteCount;

public class MemoryInformation extends Information {

    private String freePhysicalMemory;
    private String physicalMemorySize;
    private String inUseMemorySize;

    MemoryInformation() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        physicalMemorySize = humanReadableByteCount(os.getTotalPhysicalMemorySize());
        freePhysicalMemory = humanReadableByteCount(os.getFreePhysicalMemorySize());
        inUseMemorySize = humanReadableByteCount(os.getTotalPhysicalMemorySize() - os.getFreePhysicalMemorySize());
    }

    public String getFreePhysicalMemory() {
        return freePhysicalMemory;
    }

    public String getPhysicalMemorySize() {
        return physicalMemorySize;
    }

    public String getInUseMemorySize() {
        return inUseMemorySize;
    }

    @Override
    public String toString() {
        return "MemoryInformation{" +
                "freePhysicalMemory=" + freePhysicalMemory +
                ", physicalMemorySize=" + physicalMemorySize +
                ", inUseMemorySize=" + inUseMemorySize +
                '}';
    }
}
