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

/**
 * Defines filtering recommendation levels based on text noise ratio.
 *
 * This enum is used to provide user-friendly interpretations
 * of the calculated stop-word noise ratio.
 */
enum FilterRequirement {
    /** Filtering is unnecessary */
    NO_FILTER_NEEDED("No filter needed"),

    /** Filtering may or may not improve results */
    AT_YOUR_DISCRETION("At your discretion"),

    /** Filtering is strongly recommended */
    HIGHLY_DESIRABLE("A filter is highly desirable"),

    /** Filtering cannot be applied due to insufficient tokens */
    NOT_APPLICABLE("Filter Not Applicable!");

    private final String displayName;

    FilterRequirement(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns a human-readable description of the filtering requirement.
     */
    @Override
    public String toString() {
        return displayName;
    }
}

/**
 * Main application engine responsible for:
 *
 * <ul>
 * <li>loading text files and stop-word lists</li>
 * <li>preprocessing text data</li>
 * <li>managing filtering mode</li>
 * <li>calculating Sørensen–Dice similarity</li>
 * <li>analyzing stop-word noise ratios</li>
 * </ul>
 *
 * This class coordinates all components of the system and
 * acts as the central controller in a CLI-based workflow.
 */
public class TextComparator {
    /** Minimum number of unique tokens required for similarity calculation */
    public static final int MIN_TOKENS = 3;

    /** Paths to input text files and stop-word list */
    private Path textFileAPath = null;
    private Path textFileBPath = null;
    private Path stopWordsFilePath = null;

    /** Token sets extracted from the input text files */
    private Set<String> tokensA = null;
    private Set<String> tokensB = null;

    /** Stop-word filter instance (optional) */
    private StopWordFilter stopWordsFilter = null;

    /** Text preprocessing utility */
    private final TextPreprocessor textPreprocessor = new TextPreprocessor();

    /** Indicates whether stop-word filtering is enabled */
    private boolean isFiltering = false;

    /** Returns the current path to text file A */
    public Path getTextFileAPath() {
        return textFileAPath;
    }

    /** Returns the current path to text file B */
    public Path getTextFileBPath() {
        return textFileBPath;
    }

    /** Returns the current path to the stop-word list file */
    public Path getStopWordsFilePath() {
        return stopWordsFilePath;
    }

    /** Indicates whether filtering mode is currently enabled */
    public boolean getIsFiltering() {
        return isFiltering;
    }

    /**
     * Toggles stop-word filtering mode on or off.
     * Prints the updated filtering state to the console.
     */
    public void switchFilteringMode() {
        isFiltering = !isFiltering;

        // Print filtering mode
        printMsg("Current filtering mode: ", isFiltering ? "Enabled" : "Disabled");
    }

    /**
     * Ensures that a token set contains at least the minimum
     * required number of tokens.
     *
     * @throws Exception if the token set is too small
     */
    private void checkMinimumTokensNumber(Set<String> set) throws Exception {
        if (set.size() < TextComparator.MIN_TOKENS)
            throw new Exception("A minimum of three unique tokens is required!!!");
    }

    /**
     * Loads and preprocesses a text file using multiple virtual threads.
     *
     * Each line is processed independently and tokenized in parallel.
     * A progress indicator is displayed while processing is in progress.
     *
     * @param lines the lines of the input text file
     * @return a thread-safe set of unique tokens
     * @throws Exception if any task fails
     */
    private ConcurrentSkipListSet<String> multithreadUploadTextFile(List<String> lines) throws Exception {
        var tokens = new ConcurrentSkipListSet<String>();
        AtomicInteger processed = new AtomicInteger(); // Current progress counter
        int total = lines.size();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // Progress monitoring task
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

            // Parallel preprocessing tasks
            for (var line : lines) {
                // Process each line as separate task
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
     * Prompts the user to select and upload text file A.
     * The file is preprocessed and converted into a token set.
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
     * Prompts the user to select and upload text file B.
     * The file is preprocessed and converted into a token set.
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
        } catch (Exception e) {
            printErr(e);
            textFileBPath = null;
            tokensB = null;
        }
    };

    /**
     * Loads and validates a stop-word list from a file.
     *
     * Each line of the file must contain exactly one word.
     * Empty lines are ignored.
     */
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
     * Displays the current system status, including loaded files,
     * token counts, and filtering mode.
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

    /**
     * Performs Sørensen–Dice similarity comparison between
     * the two loaded text files.
     */
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

    /**
     * Prints a visual representation of a text's noise ratio.
     */
    private void printTextNoiseRatio(String label, double textNoiseRatio) {
        printMsg(label, (int) (100 * textNoiseRatio) + "%");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);
        printProgress((int) (100 * textNoiseRatio), 100);
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.println();
    }

    /**
     * Analyzes stop-word noise ratios for both texts and
     * provides a filtering recommendation.
     */
    public void noiseAnalyzer() {
        if (stopWordsFilter == null || tokensA == null || tokensB == null) {
            // Print out notification and exit
            System.out.print(ConsoleColour.RED_BOLD_BRIGHT);
            System.out.println("Please Upload Text A, Text B and Stop Word List!!!");
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);

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
