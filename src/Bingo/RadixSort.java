package Bingo;

/**
 * RADIX SORT UTILITY
 *
 * Sorts integers in O(d × (n + 10)) time, where:
 *   d = number of digits in the largest element
 *   n = array length
 *
 * Used by DPMoveStrategy to sort all candidate move scores before
 * selecting the top-ranked move.
 *
 * Approach: LSD (Least Significant Digit) Radix Sort using counting sort
 * as the stable sub-routine at each digit position.
 */
public class RadixSort {

    /**
     * Sort arr[] in ASCENDING order (non-negative integers only).
     *
     * Steps:
     *   1. Find the maximum value to know the number of digit passes.
     *   2. For each digit position (1s, 10s, 100s, ...):
     *        a. Count how many numbers have each digit (0-9).
     *        b. Build cumulative positions.
     *        c. Place elements into output[] in stable order.
     *        d. Copy output[] back to arr[].
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int max = arr[0];
        for (int v : arr) if (v > max) max = v;

        // One counting-sort pass per digit
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSortByDigit(arr, exp);
        }
    }

    private static void countingSortByDigit(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count  = new int[10]; // buckets for digits 0-9

        // Tally digit frequencies
        for (int v : arr)
            count[(v / exp) % 10]++;

        // Cumulative prefix sums → output positions
        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        // Build output right-to-left (preserves stability)
        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[--count[digit]] = arr[i];
        }

        System.arraycopy(output, 0, arr, 0, n);
    }

    // ── Paired sort (values[] sorted by their scores[]) ─────────────────────

    /**
     * Sort values[] in DESCENDING order of their scores[].
     * Scores must be non-negative integers.
     *
     * After this call, values[0] has the highest score.
     *
     * @param values  move values (numbers to call)
     * @param scores  corresponding heuristic scores (non-negative)
     */
    public static void sortByScoreDescending(int[] values, int[] scores) {
        if (values.length <= 1) return;

        int max = 0;
        for (int s : scores) if (s > max) max = s;

        // Radix sort both arrays together, keyed on scores
        for (int exp = 1; max / exp > 0; exp *= 10) {
            pairedCountSort(values, scores, exp);
        }

        // Reverse both arrays to go from descending to ascending → best first
        int lo = 0, hi = values.length - 1;
        while (lo < hi) {
            int t = values[lo]; values[lo] = values[hi]; values[hi] = t;
            t = scores[lo]; scores[lo] = scores[hi]; scores[hi] = t;
            lo++; hi--;
        }
    }

    private static void pairedCountSort(int[] values, int[] scores, int exp) {
        int n = values.length;
        int[] outVals   = new int[n];
        int[] outScores = new int[n];
        int[] count     = new int[10];

        for (int s : scores) count[(s / exp) % 10]++;
        for (int i = 1; i < 10; i++) count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            int pos = --count[(scores[i] / exp) % 10];
            outVals[pos]   = values[i];
            outScores[pos] = scores[i];
        }

        System.arraycopy(outVals,   0, values, 0, n);
        System.arraycopy(outScores, 0, scores, 0, n);
    }
}
