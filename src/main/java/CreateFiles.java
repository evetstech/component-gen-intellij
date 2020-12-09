import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CreateFiles {
    private static final List<String> templates = List.of("ComponentTemplate.txt", "TestTemplate.txt");
    public OutputResults createComponentFiles(String componentName, String outputDirectory) throws IOException {
        OutputResults result = new OutputResults();
        String outDir = outputDirectory;
        createFolders(outDir);
        createFiles(componentName, outDir);

        return result;
    }

    private void createFolders(String outputDirectory) {
        System.out.println(outputDirectory);

        File directory = new File(outputDirectory);

        if(!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void createFiles(String componentName, String outputDirectory) throws IOException {
        for (String templateFile : templates) {
            InputStream input = getClass().getResourceAsStream(templateFile);
            String result = IOUtils.toString(input, StandardCharsets.UTF_8);

            // for future use of more variables to parse
            HashMap<String, String> kvPair = new HashMap<>();
            kvPair.put("ComponentName", componentName);
            String outString = ComponentGenUtils.insertIntoTemplate(kvPair, result);

            // for future use of .ts files, story fies, etc.
            String extension;
            switch(templateFile) {
                case "TestTemplate.txt": extension = ".test.js"; break;
                default: extension = ".js"; break;
            }
            System.out.println(outputDirectory + File.separator + componentName + extension);
            Writer fileOutput = new FileWriter(outputDirectory + File.separator + componentName + extension, true);
            fileOutput.write(outString);
            fileOutput.close();
        }
    }
}