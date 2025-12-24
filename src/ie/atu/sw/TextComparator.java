package ie.atu.sw;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.AtomicInteger;

import static ie.atu.sw.ConsoleIO.*;

enum FilterRequirement {
    NO_FILTER_NEEDED("No filter needed"),
    AT_YOUR_DISCRETION("At your discretion"),
    HIGHLY_DESIRABLE("A filter is highly desirable"),
    NOT_APPLICABLE("Filter Not Applicable!");

    private final String displayName;

    FilterRequirement(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

/**
 * Main Project Class (Engine)
 */

public class TextComparator {
    // Minimum number of tokens for similarity calculation
    static final int MIN_TOKENS = 3;

    // Paths to work files
    // private Path textFileAPath = null;
    // private Path textFileBPath = null;
    // private Path stopWordsFilePath = null;
    private Path textFileAPath = Paths.get("./s.txt");
    private Path textFileBPath = Paths.get("./t.txt");
    private Path stopWordsFilePath = Paths.get("./g1000.txt");

    // Sets of words
    private Set<String> tokensA = null;
    private Set<String> tokensB = null;
    private StopWordFilter stopWordsFilter = null;

    // Text Preprocessor
    private final TextPreprocessor textPreprocessor = new TextPreprocessor();

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

    public boolean getIsFiltering() {
        return isFiltering;
    }

    public void switchFilteringMode() {
        isFiltering = !isFiltering;

        // Print filtering mode
        printMsg("Current filtering mode: ", isFiltering ? "Enabled" : "Disabled");
    }

    private void checkMinimumTokensNumber(Set<String> set) throws Exception {
        if (set.size() < TextComparator.MIN_TOKENS)
            throw new Exception("A minimum of three unique tokens is required!!!");
    }

    private ConcurrentSkipListSet<String> multithreadUploadTextFile(List<String> lines) throws Exception {
        var tokens = new ConcurrentSkipListSet<String>();
        AtomicInteger processed = new AtomicInteger();
        int total = lines.size();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            scope.fork(() -> {
                System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);

                while (!Thread.currentThread().isInterrupted()
                        && processed.get() < total) {

                    int done = processed.get();
                    printProgress(done, total);

                    try {
                        Thread.sleep(Duration.ofMillis(100));
                    } catch (InterruptedException e) {
                        break;
                    }
                }

                printProgress(total, total);
                System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
                System.out.println();
                System.out.println();

                return null;
            });

            for (var line : lines) {
                scope.fork(() -> {
                    var lineTokens = textPreprocessor.preprocess(line);
                    tokens.addAll(lineTokens);
                    processed.incrementAndGet();

                    return null;
                });
            }

            scope.join();
            scope.throwIfFailed();
        }

        return tokens;
    }

    /**
     * Prompt user to enter text A file name and upload it
     * to the system
     */

    public void uploadTextFileA() {
        // using scanner for getting input from user
        System.out.print("Input Text File A Name [Current - "
                + (textFileAPath == null ? "Not Set" : textFileAPath) + "]> ");
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
            tokensA = multithreadUploadTextFile(lines);

            checkMinimumTokensNumber(tokensA);

            // Print number uploaded words
            printMsg("Words was uploaded: ", tokensA.size());
        } catch (Exception e) {
            printErr(e);
            textFileAPath = null;
            tokensA = null;
        }
    };

    /**
     * Prompt user to enter text B file name and upload it
     * to the system
     */
    public void uploadTextFileB() {
        // using scanner for getting input from user
        System.out.print("Input Text File B Name [Current - "
                + (textFileBPath == null ? "Not Set" : textFileBPath) + "]> ");
        var s = new Scanner(System.in);

        // read user input to the fileName variable
        var fileName = s.nextLine();

        // if fileName variable is emty - return
        // else set new work file name & continue uploading
        if (fileName.length() == 0 && textFileBPath == null)
            return;

        if (fileName.length() != 0)
            textFileBPath = Paths.get(fileName);

        try {
            var lines = FileIO.readFile(textFileBPath);
            tokensB = multithreadUploadTextFile(lines);

            checkMinimumTokensNumber(tokensB);

            // Print number uploaded words
            printMsg("Words was uploaded: ", tokensB.size());
            ;
        } catch (Exception e) {
            printErr(e);
            textFileBPath = null;
            tokensB = null;
        }
    };

    public void uploadStopWordFilter() {
        // using scanner for getting input from user
        System.out.print("Input Stop Word List File Name [Current - " +
                (stopWordsFilePath == null ? "Not Set" : stopWordsFilePath) + "]> ");
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

            if (stopWordsSet.size() == 0)
                throw new Exception("Stop Word List - Empty!!!");

            stopWordsFilter = new StopWordFilter(stopWordsSet, TextComparator.MIN_TOKENS);

            // Print number uploaded words
            printMsg("Words was uploaded: ", stopWordsFilter.getSize());
        } catch (Exception e) {
            printErr(e);
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
        printMsg("Current text file A: ", textFileAPath == null ? "Not Set" : textFileAPath);

        // Print out tokens A set status
        printMsg("Current tokens A set: ", tokensA == null ? "Not Set" : tokensA.size());

        // Print out text file B name
        System.out.println();
        printMsg("Current text file B: ", textFileBPath == null ? "Not Set" : textFileBPath);

        // Print out tokens B set status
        printMsg("Current tokens B set: ", tokensB == null ? "Not Set" : tokensB.size());

        // Print out stop words list name
        System.out.println();
        printMsg("Current Stop Words file: ", stopWordsFilePath == null ? "Not Set" : stopWordsFilePath);

        // Print out Stop Words set status
        printMsg("Current Stop Words set: ", stopWordsFilter == null ? "Not Set" : stopWordsFilter.getSize());

        // Print out text Filtering Mode
        System.out.println();
        printMsg("Filtering Mode: ", isFiltering ? "Enabled" : "Disabled");
    }

    public void compareFiles() {
        try {
            if (tokensA == null)
                throw new Exception("Upload first text file!!!");

            if (tokensB == null)
                throw new Exception("Upload second text file!!!");

            if (isFiltering && stopWordsFilter == null)
                throw new Exception("Upload Stop Words List or disable filtering!!!");

            var filteredTokensA = isFiltering ? stopWordsFilter.filter(tokensA) : tokensA;
            var filteredTokensB = isFiltering ? stopWordsFilter.filter(tokensB) : tokensB;

            var similarity = DiceSimilarity.calculate(filteredTokensA, filteredTokensB);

            // Print out compearing results
            System.out.println();
            printMsg("Dice Similarity: ", String.format("%.2f", similarity));

            // Print out text Filtering Mode
            printMsg("Filtering Mode: ", isFiltering ? "Enabled" : "Disabled");
        } catch (Exception e) {
            printErr(e);
        }
    }

    private void printTextNoiseRatio(String label, double textNoiseRatio) {
        printMsg(label, (int) (100 * textNoiseRatio) + "%");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        printProgress((int) (100 * textNoiseRatio), 100);
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.println();
    }

    public void noiseAnalyzer() {
        if (stopWordsFilePath == null || tokensA == null || tokensB == null) {
            // Print out text Filtering Mode
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.println("Please Upload Text A, Text B and Stop Word List!!!");

            return;
        }

        var textANoiseRatio = 0d;
        var textBNoiseRatio = 0d;
        var isFilterNotApplicable = false;

        System.out.println("Noise Ratio Interpretation:");
        System.out.println();
        printMsg(FilterRequirement.NO_FILTER_NEEDED + ": ", "< 20%");
        printMsg(FilterRequirement.AT_YOUR_DISCRETION + ": ", "20% - 40%");
        printMsg(FilterRequirement.HIGHLY_DESIRABLE + ": ", "> 40%");

        System.out.println();
        System.out.println("Noise analysis:");

        System.out.println();
        try {
            textANoiseRatio = stopWordsFilter.calculateNoiseRatio(tokensA);

            // Print out text Filtering Mode
            printTextNoiseRatio("Text A Noise Ratio: ", textANoiseRatio);
        } catch (Exception e) {
            printErr(e);
            isFilterNotApplicable = true;
        }

        System.out.println();
        try {
            textBNoiseRatio = stopWordsFilter.calculateNoiseRatio(tokensB);

            // Print out text Filtering Mode
            printTextNoiseRatio("Text B Noise Ratio: ", textBNoiseRatio);
        } catch (Exception e) {
            printErr(e);
            isFilterNotApplicable = true;
        }

        if (isFilterNotApplicable) {
            System.out.println();
            printMsg("General conclusion: ", FilterRequirement.NOT_APPLICABLE);
            return;
        }

        var maxRatio = Math.max(textANoiseRatio, textBNoiseRatio);
        System.out.println();
        if (maxRatio < 0.2) {
            printMsg("General conclusion: ",
                    "Max noise ratio is " + (int) (100 * maxRatio) + "% - "
                            + FilterRequirement.NO_FILTER_NEEDED);
            return;
        }

        if (0.2 <= maxRatio && maxRatio <= 0.4) {
            printMsg("General conclusion: ",
                    "Max noise ratio is " + (int) (100 * maxRatio) + "% "
                            + FilterRequirement.AT_YOUR_DISCRETION);
            return;
        }

        printMsg("General conclusion: ",
                "Max noise ratio is " + (int) (100 * maxRatio) + "% " + FilterRequirement.HIGHLY_DESIRABLE);
    }
}
