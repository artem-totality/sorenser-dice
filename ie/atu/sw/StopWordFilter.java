package ie.atu.sw;

import java.util.Set;

/**
 * Stop Word Filter Class. Uses for analizing and filtering text
 */

public class StopWordFilter {
    private final Set<String> stopWords;

    public StopWordFilter(Set<String> stopWords) {
        this.stopWords = stopWords;
    };

    /**
     * Return stop words list size
     * 
     * @return size of the current stop words list
     */
    public int getSize() {
        return stopWords.size();
    }
}
