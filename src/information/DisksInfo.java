package information;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DisksInfo {

    private List<Disk> disks;

    public DisksInfo() {
        disks = new ArrayList<>();

        File[] roots = File.listRoots();

        for (File root : roots) {
            disks.add(
                    new Disk(root.getAbsolutePath(),
                            root.getTotalSpace(),
                            root.getFreeSpace(),
                            root.getUsableSpace())
            );
        }

    }

    public List<Disk> getDisks() {
        return disks;
    }

}
