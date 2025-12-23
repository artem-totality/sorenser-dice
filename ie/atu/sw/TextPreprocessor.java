package ie.atu.sw;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
        return Arrays.asList((String[]) Arrays.stream(text.split("\\s+")).filter(s -> !s.equals("")).toArray());
    }

    public List<String> preprocess(String text) {
        var normalizedText = normalizeCase(text);
        var cleanedText = removePunctuation(normalizedText);

        return tokenize(cleanedText);
    }
}
