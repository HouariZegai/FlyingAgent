package information;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RawInfo {

    private String rawString;

    public RawInfo() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("systeminfo");
        BufferedReader systemInformationReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = systemInformationReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(System.lineSeparator());
        }

        this.rawString = stringBuilder.toString().trim();
    }

    public String getRawString() {
        return rawString;
    }

    @Override
    public String toString() {
        return rawString;
    }
}
