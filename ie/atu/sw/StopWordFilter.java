package ie.atu.sw;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Stop Word Filter Class. Uses for analizing and filtering text
 */

public class StopWordFilter {
    // Stop words list
    private final Set<String> stopWords;
    // Minimum number of tokens for similarity calculation
    private final int MIN_TOKENS;

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
}
