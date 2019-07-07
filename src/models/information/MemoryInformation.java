package models.information;

import com.sun.management.OperatingSystemMXBean;

import java.io.Serializable;
import java.lang.management.ManagementFactory;

import static models.information.Utils.humanReadableByteCount;

public class MemoryInformation extends Information implements Serializable {

    private long freePhysicalMemory;
    private long physicalMemorySize;
    private long inUseMemorySize;

    MemoryInformation() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        physicalMemorySize = (os.getTotalPhysicalMemorySize());
        freePhysicalMemory = (os.getFreePhysicalMemorySize());
        inUseMemorySize = (physicalMemorySize - freePhysicalMemory);
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
                "freePhysicalMemory=" + humanReadableByteCount(freePhysicalMemory) +
                ", physicalMemorySize=" + humanReadableByteCount(physicalMemorySize) +
                ", inUseMemorySize=" + humanReadableByteCount(inUseMemorySize) +
                '}';
    }
}
