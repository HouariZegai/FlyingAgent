package information;

public class Disk extends Information {

    private String name;
    private long totalSpace, freeSpace, usableSpace;

    Disk(String name, long totalSpace, long freeSpace) {
        this.name = name;
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.usableSpace = totalSpace - freeSpace;
    }

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return "Disk{" +
                "name='" + name + '\'' +
                ", totalSpace=" + humanReadableByteCount(totalSpace) +
                ", freeSpace=" + humanReadableByteCount(freeSpace) +
                ", usableSpace=" + humanReadableByteCount(usableSpace) +
                '}';
    }
}
