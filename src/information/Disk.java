package information;

import static information.Utils.humanReadableByteCount;

public class Disk extends Information {

    private String name;
    private String totalSpace;
    private String freeSpace;
    private String usableSpace;

    Disk(String name, long totalSpace, long freeSpace) {
        this.name = name;
        this.totalSpace = humanReadableByteCount(totalSpace);
        this.freeSpace = humanReadableByteCount(freeSpace);
        this.usableSpace = humanReadableByteCount(totalSpace - freeSpace);
    }

    public String getTotalSpace() {
        return totalSpace;
    }

    public String getFreeSpace() {
        return freeSpace;
    }

    public String getUsableSpace() {
        return usableSpace;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Disk{" +
                "name='" + name + '\'' +
                ", totalSpace=" + (totalSpace) +
                ", freeSpace=" + (freeSpace) +
                ", usableSpace=" + (usableSpace) +
                '}';
    }
}
