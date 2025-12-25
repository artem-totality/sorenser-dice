package ie.atu.sw;

import java.util.Set;
import java.util.TreeSet;

/**
 * Stop-word filtering and noise analysis utility.
 *
 * This class is responsible for:
 * <ul>
 * <li>removing stop-words from a set of tokens</li>
 * <li>calculating the stop-word noise ratio of a text</li>
 * </ul>
 *
 * The class also enforces a minimum number of meaningful tokens
 * required for similarity calculation. If this condition is violated,
 * an exception is thrown to prevent invalid comparisons.
 */
public class StopWordFilter {
    /**
     * Set of stop-words used for filtering and noise analysis.
     */
    private final Set<String> stopWords;

    /**
     * Minimum number of tokens required after filtering.
     * Used as a safety threshold to avoid meaningless similarity calculations.
     */
    private final int MIN_TOKENS;

    /**
     * Creates a stop-word filter with a predefined stop-word list and
     * a minimum token threshold.
     *
     * @param stopWords  the set of stop-words
     * @param min_tokens the minimum number of tokens required after filtering
     */
    public StopWordFilter(Set<String> stopWords, int min_tokens) {
        this.stopWords = stopWords;
        this.MIN_TOKENS = min_tokens;
    };

    /**
     * Return stop words list size
     * 
     * @return size of the current stop words list
     */
    public int getSize() {
        return stopWords.size();
    }

    /**
     * Filters stop-words from the provided token set.
     *
     * All tokens present in the stop-word list are removed.
     *
     * If the number of remaining tokens falls below the configured
     * minimum threshold, an exception is thrown
     *
     * @param tokens the original set of tokens
     * @return a new set of tokens with stop-words removed
     * @throws Exception if too few tokens remain after filtering
     */
    public Set<String> filter(Set<String> tokens) throws Exception {
        var filteredTokens = new TreeSet<String>();

        var iter = tokens.iterator();

        while (iter.hasNext()) {
            var token = iter.next();
            if (!stopWords.contains(token))
                filteredTokens.add(token);
        }

        if (filteredTokens.size() < MIN_TOKENS)
            throw new Exception("Too few tokens after filtering - Disable Filtering!!!");

        return filteredTokens;
    }

    /**
     * Calculates the noise ratio of a token set based on stop-words.
     *
     * The noise ratio is defined as:
     * 
     * <pre>
     *     noise = number of stop-words / total number of tokens
     * </pre>
     *
     * This metric indicates how much of the text consists of
     * non-informative (stop) words.
     *
     * If removing stop-words would result in fewer tokens than
     * the configured minimum threshold, an exception is thrown.
     *
     * @param tokens the set of tokens to analyze
     * @return the noise ratio in the range {@code [0.0, 1.0]}
     * @throws Exception if too few tokens would remain after filtering
     */
    public double calculateNoiseRatio(
            Set<String> tokens) throws Exception {
        var noiseCounter = 0d;
        var iter = tokens.iterator();

        while (iter.hasNext()) {
            var token = iter.next();

            if (stopWords.contains(token))
                noiseCounter += 1;
        }

        if (tokens.size() - noiseCounter < MIN_TOKENS)
            throw new Exception("Too few tokens after filtering!!!");

        return noiseCounter / tokens.size();
    }
}
