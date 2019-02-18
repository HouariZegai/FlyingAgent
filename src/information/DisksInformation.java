package information;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DisksInformation extends Information implements Serializable {

    private List<Disk> disks;

    DisksInformation() {
        disks = new ArrayList<>();

        File[] roots = File.listRoots();

        for (File root : roots) {
            disks.add(
                    new Disk(root.getAbsolutePath(),
                            root.getTotalSpace(),
                            root.getFreeSpace())
            );
        }

    }

    public List<Disk> getDisks() {
        return disks;
    }

    @Override
    public String toString() {
        return "DisksInformation{" +
                "disks=" + disks +
                '}';
    }
}
