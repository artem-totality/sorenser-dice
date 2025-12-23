package ie.atu.sw;

import java.util.Set;

public class DiceSimilarity {
    /**
     * Calculates Sørensen–Dice similarity
     * 
     * @param tokensA - first tokens set
     * @param tokensB - second tokens set
     * @return similarity coefficient
     */
    public static double calculate(
            Set<String> tokensA,
            Set<String> tokensB) {

        return 2d * DiceSimilarity.intersectionSize(tokensA, tokensB) / (tokensA.size() + tokensB.size());
    }

    /**
     * Calculates the size of the intersection of two sets
     * 
     * @param a - first tokens set
     * @param b - second tokens set
     * @return number of common elements
     */
    private static int intersectionSize(
            Set<String> a,
            Set<String> b) {

        var intersection = 0;

        // For optimization purposes, we iterate over a smaller set of tokens
        if (a.size() < b.size()) {
            var iter = a.iterator();

            while (iter.hasNext()) {
                var token = iter.next();

                if (b.contains(token))
                    intersection += 1;
            }
        } else {
            var iter = b.iterator();

            while (iter.hasNext()) {
                var token = iter.next();

                if (a.contains(token))
                    intersection += 1;
            }
        }

        return intersection;
    }
}
