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
    private StopWordFilter stopWordsFilter = null;

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

    /**
     * Prompt user to enter text A file name and upload it
     * to the system
     */

    public void uploadTextFileA() {
        // using scanner for getting input from user
        System.out.print("Input Text File A Name [Current - "
                + (getTextFileAPath() == null ? "Not Set" : getTextFileAPath()) + "]> ");
        var s = new Scanner(System.in);

        // read user input to the fileName variable
        var fileName = s.nextLine();

        // if fileName variable is emty - return
        // else set new work file name & continue uploading
        if (fileName.length() == 0 && textFileAPath == null)
            return;
        if (fileName.length() != 0)
            textFileAPath = Paths.get(fileName);

        try {
            var lines = FileIO.readFile(textFileAPath);
            tokensA = new TreeSet<String>(lines);

            // Print number uploaded words
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Was uploaded: ");
            System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
            System.out.print(tokensA.size());

            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.println(" words");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            textFileAPath = null;
            tokensA = null;
        }
    };

    public void uploadStopWordFilter() {
        // using scanner for getting input from user
        System.out.print("Input Stop Word List File Name > ");
        var s = new Scanner(System.in);

        // read user input to the fileName variable
        var fileName = s.nextLine();

        // if fileName variable is emty - return
        // else set new work file name & continue uploading
        if (fileName.length() == 0 && stopWordsFilePath == null)
            return;
        if (fileName.length() != 0)
            stopWordsFilePath = Paths.get(fileName);

        try {
            var lines = FileIO.readFile(stopWordsFilePath);
            var stopWordsSet = new TreeSet<String>();

            // Try to parse Stop Words List. Every lien has to consist exactly one word
            // else throw Exception
            for (var line : lines) {
                var words = line.toLowerCase().split("\\s+");
                // Empty line - continue
                if (words.length == 0)
                    continue;

                // More than one word - Format Error !!!
                if (words.length > 1)
                    throw new Exception("Stop Words List File - Format Error!!!");

                // If not empty string - add word to stop words set
                if (!words[0].equals(""))
                    stopWordsSet.add(words[0]);
            }

            stopWordsFilter = new StopWordFilter(stopWordsSet);

            // Print number uploaded words
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Was uploaded: ");
            System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
            System.out.print(stopWordsFilter.getSize());

            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.println(" words");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            stopWordsFilePath = null;
            stopWordsFilter = null;
        }
    }

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

        // Print out tokens B set status
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Current tokens B set: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(tokensB == null ? "Not Set" : (tokensB.size() == 0 ? "Empty" : tokensB.size()));

        // Print out stop words list name
        System.out.println();
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Current stop words list file: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(stopWordsFilePath == null ? "Not Set" : stopWordsFilePath);

        // Print out Stop Words set status
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Current Stop Words set: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(stopWordsFilter == null ? "Not Set"
                : (stopWordsFilter.getSize() == 0 ? "Empty" : stopWordsFilter.getSize()));

        // Print out text Filtering Mode
        System.out.println();
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Filtering Mode: ");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        System.out.println(isFiltering ? "Enabled" : "Disabled");

        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
    }
}
