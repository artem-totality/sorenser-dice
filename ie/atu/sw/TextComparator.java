package ie.atu.sw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Main Project Class (Engine)
 */

public class TextComparator {
    // Paths to work files
    private Path textFileA = null;
    private Path textFileB = null;
    private Path stopWordsFile = null;

    // Filtering mode
    private boolean isFiltering = false;

    public void setTextFileB(String textFileBPath) {
        this.textFileB = Paths.get(textFileBPath);
    }

    public void setStopWordsFile(String stopWordsFilePath) {
        this.stopWordsFile = Paths.get(stopWordsFilePath);
    }

    public Path getTextFileA() {
        return textFileA;
    }

    public Path getTextFileB() {
        return textFileB;
    }

    public Path getStopWordsFile() {
        return stopWordsFile;
    }

    public void uploadTextFileA() {
        // using scanner for getting input from user
        System.out.print("Input Text File A Name> ");
        try (var s = new Scanner(System.in)) {
            // read user input to the fileName variable
            var fileName = s.nextLine();

            // if fileName variable is emty - return
            // else set new work file name & continue uploading
            if (fileName.length() == 0)
                return;

            textFileA = Paths.get(fileName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            textFileA = null;
        }

    };

    /**
     * Show current system status: File A, File B, Stop Words List
     * and Using Stop Words List Mode
     * 
     */

    public void getSystemStatus() {
        // Print out text file A name
        System.out.println("Current mapping file: " + (textFileA == null ? "Not Set" : textFileA));

        // Print out text file B name
        System.out.println();
        System.out.println("Current mapping file: " + (textFileB == null ? "Not Set" : textFileB));

        // Print out text Filtering Mode
        System.out.println();
        System.out.println("Filtering Mode: " + (isFiltering ? "Enabled" : "Disabled"));
    }
}
