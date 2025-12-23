package ie.atu.sw;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    public void setTextFileA(String textFileAPath) {
        this.textFileA = Paths.get(textFileAPath);
    }

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
        System.out.println("Upload Text File A");
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
