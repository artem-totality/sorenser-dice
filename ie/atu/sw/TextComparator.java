package ie.atu.sw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * Main Project Class (Engine)
 */

public class TextComparator {
    // Paths to work files
    private Path textFileAPath = null;
    private Path textFileBPath = null;
    private Path stopWordsFilePath = null;

    // Sets of words
    private Set<String> tokensA = null;
    private Set<String> tokensB = null;
    private Set<String> stopWords = null;

    // Filtering mode
    private boolean isFiltering = false;

    public Path getTextFileAPath() {
        return textFileAPath;
    }

    public Path getTextFileBPath() {
        return textFileBPath;
    }

    public Path getStopWordsFilePath() {
        return stopWordsFilePath;
    }

    public void uploadTextFileA() {
        // using scanner for getting input from user
        System.out.print("Input Text File A Name > ");
        var s = new Scanner(System.in);

        // read user input to the fileName variable
        var fileName = s.nextLine();

        // if fileName variable is emty - return
        // else set new work file name & continue uploading
        if (fileName.length() == 0 && textFileAPath == null)
            return;

        textFileAPath = Paths.get(fileName);

        try {
            var lines = FileIO.readFile(textFileAPath);
            tokensA = new TreeSet<String>(lines);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            textFileAPath = null;
            tokensA = null;
        }
    };

    /**
     * Show current system status: File A, File B, Stop Words List
     * and Using Stop Words List Mode
     * 
     */

    public void getSystemStatus() {
        // Print out text file A name
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Current text file A: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(textFileAPath == null ? "Not Set" : textFileAPath);

        // Print out tokens A set status
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Current tokens A set: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(tokensA == null ? "Not Set" : (tokensA.size() == 0 ? "Empty" : tokensA.size()));

        // Print out text file B name
        System.out.println();
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Current text file B: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(textFileBPath == null ? "Not Set" : textFileBPath);

        // Print out text Filtering Mode
        System.out.println();
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Filtering Mode: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(isFiltering ? "Enabled" : "Disabled");

        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
    }
}
