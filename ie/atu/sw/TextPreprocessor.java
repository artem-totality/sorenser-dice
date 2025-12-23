package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;

public class TextPreprocessor {
    private String normalizeCase(String text) {
        return text.toLowerCase();
    };

    private String removePunctuation(String text) {
        return text.replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

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

    public List<String> preprocess(String text) {
        var normalizedText = normalizeCase(text);
        var cleanedText = removePunctuation(normalizedText);

        return tokenize(cleanedText);
    }
}
