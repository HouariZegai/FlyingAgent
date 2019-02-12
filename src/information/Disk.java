package information;

import static information.Utils.humanReadableByteCount;

public class Disk extends Information {

    private String name;
    private long totalSpace;
    private long freeSpace;
    private long usableSpace;

    Disk(String name, long totalSpace, long freeSpace) {
        this.name = name;
        this.totalSpace = (totalSpace);
        this.freeSpace = (freeSpace);
        this.usableSpace = (totalSpace - freeSpace);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Disk{" +
                "name='" + name + '\'' +
                ", totalSpace=" + humanReadableByteCount(totalSpace) +
                ", freeSpace=" + humanReadableByteCount(freeSpace) +
                ", usableSpace=" + humanReadableByteCount(usableSpace) +
                '}';
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public long getUsableSpace() {
        return usableSpace;
    }
}
