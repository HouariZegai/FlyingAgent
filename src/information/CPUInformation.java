package information;

import com.google.gson.Gson;

public class CPUInformation extends Information {

    private String processorId;
    private String processorArchitecture;
    private String numberCores;

    public CPUInformation() {
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
        return "CPUInformation{" +
                "processorId='" + processorId + '\'' +
                ", processorArchitecture='" + processorArchitecture + '\'' +
                ", numberCores='" + numberCores + '\'' +
                '}';
    }
}
