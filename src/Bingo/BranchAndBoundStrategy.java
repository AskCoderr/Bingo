package Bingo;

import java.util.PriorityQueue;

/**
 * BRANCH AND BOUND STRATEGY — Hard Difficulty
 *
 * Branch and Bound (B&B) is a systematic optimisation technique that
 * combines:
 *   • BRANCHING  — splitting the problem into sub-problems (candidate moves)
 *   • BOUNDING   — computing an optimistic upper bound for each branch
 *   • PRUNING    — discarding any branch whose upper bound ≤ best score found
 *
 * HOW IT WORKS HERE:
 *   Goal: find the number to call that maximises the computer's advantage.
 *
 *   1. BRANCH: for every uncalled number v, create a "node" representing
 *              the state that results from the computer calling v.
 *
 *   2. BOUND:  For each node compute:
 *              lowerBound = actual net score after calling v
 *                           (evaluate(computer) − evaluate(player))
 *              upperBound = lowerBound
 *                           + sum of potential gains from all incomplete lines
 *                           (optimistically assumes every remaining call
 *                            perfectly fills a line on the computer's board)
 *
 *   3. EXPLORE best-first using a max-priority queue (highest upper bound first).
 *              After the first node is processed its lowerBound becomes the
 *              current "best". Any subsequent node with upperBound ≤ best
 *              is PRUNED — guaranteed to never produce a better answer.
 *
 *   4. RETURN the value associated with the highest lowerBound found.
 *
 * WHY IT PRUNES:
 *   The priority queue delivers nodes in decreasing upper-bound order.
 *   The moment we pop a node whose upperBound ≤ bestScore, we know that
 *   EVERY remaining node in the queue also has upperBound ≤ bestScore
 *   (since the queue is sorted). So we can break immediately — cutting the
 *   rest of the search.
 */
public class BranchAndBoundStrategy implements MoveStrategy {

    /**
     * A node in the Branch and Bound search tree.
     * Ordered by upperBound descending so PriorityQueue delivers the
     * most-promising branch first.
     */
    private static class BBNode implements Comparable<BBNode> {
        final int value;       // the number to call
        final int lowerBound;  // actual net score (guaranteed achievable)
        final int upperBound;  // optimistic ceiling (if ≤ best → prune)

        BBNode(int v, int lb, int ub) {
            value      = v;
            lowerBound = lb;
            upperBound = ub;
        }

        @Override
        public int compareTo(BBNode o) {
            // Max-heap: node with highest upperBound comes first
            return Integer.compare(o.upperBound, this.upperBound);
        }
    }

    // ── Main entry point ────────────────────────────────────────────────────

    @Override
    public int findBestMove(BingoBoard computer, BingoBoard player) {

        int bestScore = Integer.MIN_VALUE;
        int bestVal   = -1;

        // ── 1. BRANCH: generate a node for every uncalled number ─────────────
        PriorityQueue<BBNode> pq = new PriorityQueue<>();

        for (int i = 0; i < computer.size; i++) {
            for (int j = 0; j < computer.size; j++) {
                if (!computer.nodes[i][j].marked) {
                    int val = computer.nodes[i][j].value;

                    // Simulate calling this number on both boards
                    computer.nodes[i][j].marked = true;
                    boolean pHad = markByValue(player, val);

                    int lb = netScore(computer, player);       // actual score
                    int ub = lb + potentialBonus(computer);    // optimistic ceiling

                    // Backtrack (undo simulation)
                    computer.nodes[i][j].marked = false;
                    unmarkByValue(player, val, pHad);

                    pq.add(new BBNode(val, lb, ub));
                }
            }
        }

        // ── 2. EXPLORE + BOUND ────────────────────────────────────────────────
        while (!pq.isEmpty()) {
            BBNode node = pq.poll();   // highest upper bound first

            // ── 3. PRUNE: upper bound can't beat current best → stop ──────────
            if (node.upperBound <= bestScore) break; // rest of PQ is also ≤ best

            // Accept this branch's actual (lower bound) score
            if (node.lowerBound > bestScore) {
                bestScore = node.lowerBound;
                bestVal   = node.value;
            }
        }

        return bestVal != -1 ? bestVal : firstUnmarked(computer);
    }

    // ── Score helpers ────────────────────────────────────────────────────────

    /** Net score = boardScore(computer) − boardScore(player). */
    private int netScore(BingoBoard c, BingoBoard p) {
        return boardScore(c) - boardScore(p);
    }

    /**
     * Optimistic bonus: for every INCOMPLETE line on the computer's board,
     * compute the gain assuming the very next call completes it.
     * This is always an over-estimate (hence a valid upper bound).
     */
    private int potentialBonus(BingoBoard b) {
        int bonus = 0;
        int n = b.size;
        for (int i = 0; i < n; i++) bonus += lineBonus(b, i, true);   // rows
        for (int i = 0; i < n; i++) bonus += lineBonus(b, i, false);  // cols
        bonus += diagBonus(b, true);   // main diagonal
        bonus += diagBonus(b, false);  // anti-diagonal
        return bonus;
    }

    private int lineBonus(BingoBoard b, int idx, boolean isRow) {
        int marked = 0;
        for (int k = 0; k < b.size; k++) {
            boolean m = isRow ? b.nodes[idx][k].marked : b.nodes[k][idx].marked;
            if (m) marked++;
        }
        if (marked == b.size) return 0; // already complete, no extra bonus
        // Optimistic: next call makes this line one step closer to 1,000,000
        return 1_000_000 - (int) Math.pow(10, marked);
    }

    private int diagBonus(BingoBoard b, boolean main) {
        int marked = 0;
        for (int i = 0; i < b.size; i++) {
            int j = main ? i : b.size - 1 - i;
            if (b.nodes[i][j].marked) marked++;
        }
        if (marked == b.size) return 0;
        return 1_000_000 - (int) Math.pow(10, marked);
    }

    /** Total positional score across all lines of a board. */
    private int boardScore(BingoBoard b) {
        int score = 0, n = b.size;
        for (int i = 0; i < n; i++) score += lineScore(b, i, true);
        for (int i = 0; i < n; i++) score += lineScore(b, i, false);
        score += diagScore(b, true);
        score += diagScore(b, false);
        return score;
    }

    private int lineScore(BingoBoard b, int idx, boolean isRow) {
        int cnt = 0;
        for (int k = 0; k < b.size; k++) {
            boolean m = isRow ? b.nodes[idx][k].marked : b.nodes[k][idx].marked;
            if (m) cnt++;
        }
        if (cnt == b.size) return 1_000_000;
        return (int) Math.pow(10, cnt);
    }

    private int diagScore(BingoBoard b, boolean main) {
        int cnt = 0;
        for (int i = 0; i < b.size; i++) {
            int j = main ? i : b.size - 1 - i;
            if (b.nodes[i][j].marked) cnt++;
        }
        if (cnt == b.size) return 1_000_000;
        return (int) Math.pow(10, cnt);
    }

    // ── Board simulation helpers ─────────────────────────────────────────────

    private boolean markByValue(BingoBoard b, int val) {
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                if (b.nodes[i][j].value == val && !b.nodes[i][j].marked) {
                    b.nodes[i][j].marked = true;
                    return true;
                }
        return false;
    }

    private void unmarkByValue(BingoBoard b, int val, boolean wasMarked) {
        if (!wasMarked) return;
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                if (b.nodes[i][j].value == val) {
                    b.nodes[i][j].marked = false;
                    return;
                }
    }

    private int firstUnmarked(BingoBoard b) {
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                if (!b.nodes[i][j].marked)
                    return b.nodes[i][j].value;
        return -1;
    }
}
