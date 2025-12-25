package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for low-level text preprocessing.
 *
 * This class performs mechanical transformations on raw text:
 * <ul>
 * <li>normalization to lower case</li>
 * <li>removal of punctuation and special characters</li>
 * <li>tokenization into individual words</li>
 * </ul>
 *
 * The class does not apply any semantic logic such as stop-word filtering
 * or similarity calculation. It prepares text for further analysis.
 */
public class TextPreprocessor {

    /**
     * Converts all characters in the input text to lower case.
     * 
     * This ensures that further text comparisons are case-insensitive.
     *
     * @param text the input text
     * @return the text converted to lower case
     */
    private String normalizeCase(String text) {
        return text.toLowerCase();
    };

    /**
     * Removes punctuation and unwanted characters from the text.
     *
     * The method performs the following steps:
     * <ul>
     * <li>removes all characters except letters, digits, whitespace and
     * hyphens</li>
     * <li>replaces multiple whitespace characters with a single space</li>
     * <li>removes standalone hyphens and hyphens surrounded by whitespace</li>
     * <li>trims leading and trailing whitespace</li>
     * </ul>
     *
     * The result is a clean, whitespace-separated string suitable for tokenization.
     *
     * @param text the normalized input text
     * @return cleaned text without punctuation
     */
    private String removePunctuation(String text) {
        var cleaned = text.replaceAll("[^\\p{L}\\p{N}\\s-]", " ") // remove punctuation
                .replaceAll("\\s+", " ") // multiple spaces into one
                .replaceAll("\\s-\\s|-\\s|\\s-", " ") // removing free-standing hyphens
                .trim();

        return cleaned;
    }

    /**
     * Splits the cleaned text into individual word tokens.
     *
     * The text is split on one or more whitespace characters.
     * Any empty tokens that may result from splitting are explicitly removed.
     *
     * @param text the cleaned text
     * @return a list of non-empty word tokens
     */
    private List<String> tokenize(String text) {
        // Divide text on tokens
        var tokens = text.split("\\s+");
        var filteredTokens = new ArrayList<String>();

        // Filter result from empty string tokens
        for (var token : tokens)
            if (!token.equals(""))
                filteredTokens.add(token);

        return filteredTokens;
    }

    /**
     * Performs the full preprocessing pipeline on the input text.
     *
     * The pipeline consists of:
     * <ol>
     * <li>normalizing the text to lower case</li>
     * <li>removing punctuation and unwanted characters</li>
     * <li>tokenizing the cleaned text into words</li>
     * </ol>
     *
     * This method is intended to be the main entry point for text preprocessing.
     *
     * @param text the raw input text
     * @return a list of processed word tokens
     */
    public List<String> preprocess(String text) {
        var normalizedText = normalizeCase(text);
        var cleanedText = removePunctuation(normalizedText);

        return tokenize(cleanedText);
    }
}
