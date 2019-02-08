package information;

public class CpuInfo {

    private String processorId;
    private String processorArchitecture;
    private String numberCores;

    public CpuInfo() {
        processorId = System.getenv("PROCESSOR_IDENTIFIER");
        processorArchitecture = System.getenv("PROCESSOR_ARCHITECTURE");
        numberCores = System.getenv("NUMBER_OF_PROCESSORS");
    }

    public String getProcessorId() {
        return processorId;
    }

    public String getProcessorArchitecture() {
        return processorArchitecture;
    }

    public String getNumberCores() {
        return numberCores;
    }

    @Override
    public String toString() {
        return "CpuInfo{" +
                "processorId='" + processorId + '\'' +
                ", processorArchitecture='" + processorArchitecture + '\'' +
                ", numberCores='" + numberCores + '\'' +
                '}';
    }
}
