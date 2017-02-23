package tex.util;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Set;

/**
 * Use in replacing special characters in a TEX file
 * to its equivalent
 *
 * @author Clyde M. Velasquez
 * @since January 31, 2017
 * @version 0.1
 */
public class TexUtil {
    private static final File CURRENT = new File("");

    public static void main(String[] args) {
        if (args.length == 0 || args.length > 1) {
            message();
        } else if (args.length == 1) {
            // Check args if it contains tex files
            File texFile = new File(args[0]);
            if (texFile.exists()) {
                if (!texFile.isFile() && !texFile.getAbsolutePath().trim().endsWith(".tex")) {
                    System.err.println("ERROR: Error processing " + texFile.getAbsolutePath());
                    System.err.println("Make sure it's a TEX file!");
                    return;
                }
            } else {
                System.err.println("ERROR: File does not exist!");
                return;
            }

            StringBuilder sb = new StringBuilder();
            System.out.println("Processing " + texFile.getAbsolutePath() + " ...");
            if (texFile.exists()) {
                try (BufferedReader bReader = new BufferedReader(new FileReader(texFile))) {
                    bReader.lines().forEachOrdered(line -> sb.append(line).append("\n"));
                    System.out.println("Done!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String fileContents = sb.toString();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(texFile))) {
                Set<String> keys = dictionary().keySet();
                for (String key : keys) {
                    fileContents = fileContents.replaceAll(key, dictionary().get(key));
                }

                writer.write(fileContents);
                writer.close();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static void message() {
        System.out.println("USAGE: java -jar TexUtil.jar file1.tex");
        System.out.println("Handles only one TEX file as of this time.");
        System.out.println("special_characters.csv must be the same directory as TexUtil.jar");
    }

    private static HashMap<String, String> dictionary() throws URISyntaxException, IOException {
        HashMap<String, String> dictionary = new HashMap<>();

        File specialCharactersCSV = new File(CURRENT + "special_characters.csv");
        CSVReader reader = new CSVReader(specialCharactersCSV);
        while (reader.hasNextLine()) {
            String[] value = reader.lineSplit();
            dictionary.put(value[0], value[1]);
        }

        return dictionary;
    }
}