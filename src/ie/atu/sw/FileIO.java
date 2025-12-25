package ie.atu.sw;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Helper Class for Input/Output with files
 */
public class FileIO {

    /**
     * Method read text file and return result as List of String
     * 
     * @param filePath - path to reading text file
     * @return List of strings if the file reading is successful, otherwise throws
     *         Exception
     * @throws Exception if reading the file was unsuccessful
     */
    public static List<String> readFile(Path filePath) throws Exception {
        try {
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("Error reading file: " + filePath);
        }
    }
}