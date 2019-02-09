package information;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

import static information.Utils.humanReadableByteCount;

public class MemoryInformation extends Information {

    private long freePhysicalMemory;
    private long physicalMemorySize;
    private long inUseMemorySize;

    MemoryInformation() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        physicalMemorySize = (os.getTotalPhysicalMemorySize());
        freePhysicalMemory = (os.getFreePhysicalMemorySize());
        inUseMemorySize = (physicalMemorySize - freePhysicalMemory);
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
