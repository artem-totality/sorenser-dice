package ie.atu.sw;

import java.util.Set;

/**
 * Utility class for calculating text similarity using the Sørensen–Dice
 * coefficient.
 *
 * This implementation operates on sets of unique tokens.
 */
public class DiceSimilarity {
    /**
     * Calculates the Sørensen–Dice similarity coefficient for two token sets.
     *
     * The coefficient is defined as:
     * 
     * <pre>
     *     Dice = 2 × |A ∩ B| / (|A| + |B|)
     * </pre>
     *
     * This method assumes that both input sets are non-null.
     *
     * @param tokensA the first set of tokens
     * @param tokensB the second set of tokens
     * @return the Sørensen–Dice similarity coefficient
     */
    public static double calculate(
            Set<String> tokensA,
            Set<String> tokensB) {

        return 2d * DiceSimilarity.intersectionSize(tokensA, tokensB) / (tokensA.size() + tokensB.size());
    }

    /**
     * Calculates the size of the intersection of two token sets.
     *
     * For performance reasons, the method always iterates over the smaller
     * of the two sets and checks membership in the larger set.
     *
     * This method also produces console output showing the progress of the
     * intersection calculation.
     *
     * @param a the first token set
     * @param b the second token set
     * @return the number of common elements in both sets
     */
    private static int intersectionSize(
            Set<String> a,
            Set<String> b) {

        var intersection = 0;
        var counter = 0;

        System.out.println("Process:");
        System.out.print(ConsoleColour.YELLOW_BOLD_BRIGHT);

        // For optimization purposes, we iterate over a smaller set of tokens
        if (a.size() < b.size()) {
            var iter = a.iterator();
            var total = a.size();

            while (iter.hasNext()) {
                var token = iter.next();

                if (b.contains(token))
                    intersection += 1;

                counter += 1;
                ConsoleIO.printProgress(counter, total);
            }
        } else {
            var iter = b.iterator();
            var total = b.size();

            while (iter.hasNext()) {
                var token = iter.next();

                if (a.contains(token))
                    intersection += 1;

                counter += 1;
                ConsoleIO.printProgress(counter, total);
            }
        }

        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.println();

        return intersection;
    }
}
