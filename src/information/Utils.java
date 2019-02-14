package information;

public final class Utils {

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static Double humanReadableByteCountNumber(long bytes) {
        int unit = 1024;
        if (bytes < unit) return (double) bytes;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "kMGTPE".charAt(exp - 1) + "";
        return Double.valueOf(String.format("%.1f", bytes / Math.pow(unit, exp)));
    }
}
