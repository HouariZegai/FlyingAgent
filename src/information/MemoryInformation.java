package information;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class MemoryInformation extends Information {

    private long freePhysicalMemory, physicalMemorySize, inUseMemorySize;

    MemoryInformation() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        physicalMemorySize = os.getTotalPhysicalMemorySize();
        freePhysicalMemory = os.getFreePhysicalMemorySize();
        inUseMemorySize = physicalMemorySize - freePhysicalMemory;
    }

    public long getFreePhysicalMemory() {
        return freePhysicalMemory;
    }

    public long getPhysicalMemorySize() {
        return physicalMemorySize;
    }

    public long getInUseMemorySize() {
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
