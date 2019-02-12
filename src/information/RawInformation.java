package information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RawInformation {

    private String rawString;

    public RawInformation() {
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("systeminfo");

            BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = systemInformationReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                this.rawString = stringBuilder.toString().trim();
            }
        } catch (IOException e) {
            rawString = null;
        }
    }

    public String getRawString() {
        return rawString;
    }

    @Override
    public String toString() {
        return rawString;
    }
}
