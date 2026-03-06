package Bingo;

import java.util.HashMap;
import java.util.Map;

/**
 * HARD difficulty: Minimax algorithm with Alpha-Beta pruning and Memoization.
 *
 * BACKTRACKING EXPLAINED:
 *   This class implements classic backtracking search over the Bingo game tree.
 *   At each step, it simulates calling a number (marks it on both boards), evaluates
 *   the resulting state recursively, then UNDOES (backtracks) that mark before trying
 *   the next candidate. Alpha-Beta pruning cuts branches that cannot improve the
 *   current best — dramatically reducing nodes visited.
 *
 * ALGORITHM FLOW:
 *   findBestMove()
 *     └─ For each uncalled number (MAX node - computer picks):
 *          mark on both boards           ← SIMULATE
 *          minimax(depth-1, MIN=false)   ← RECURSE
 *            └─ For each uncalled number (MIN node - player picks):
 *                 mark on both boards    ← SIMULATE
 *                 minimax(depth-2, MAX)  ← RECURSE
 *                 unmark both boards     ← BACKTRACK
 *          unmark both boards            ← BACKTRACK
 *     return value with highest score
 *
 * HEURISTIC:
 *   NetScore = evaluate(computer) − evaluate(player)
 *   Lines with more marked cells score exponentially higher (10^k).
 *   A WINNING line is worth 1,000,000 points (decisive).
 */
public class MinimaxStrategy implements MoveStrategy {

    // Search depth. 4 is responsive even on 5x5 with alpha-beta pruning.
    private static final int DEPTH = 4;

    // Memoization table: board state string → minimax score
    private final Map<String, Integer> memo = new HashMap<>();

    @Override
    public int findBestMove(BingoBoard computer, BingoBoard player) {
        memo.clear(); // Fresh memo for each turn (board state has changed)

        int bestVal   = -1;
        int bestScore = Integer.MIN_VALUE;
        int alpha     = Integer.MIN_VALUE;
        int beta      = Integer.MAX_VALUE;

        // ── MAX level: computer chooses a number to call ──────────────────────
        for (int i = 0; i < computer.size; i++) {
            for (int j = 0; j < computer.size; j++) {
                if (!computer.nodes[i][j].marked) {
                    int val = computer.nodes[i][j].value;

                    // ── SIMULATE ──────────────────────────────────
                    computer.nodes[i][j].marked = true;
                    boolean playerHad = markByValue(player, val);

                    // Recurse into MIN layer (player's optimal response)
                    int score = minimax(computer, player, DEPTH - 1, false, alpha, beta);

                    // ── BACKTRACK ─────────────────────────────────
                    computer.nodes[i][j].marked = false;
                    unmarkByValue(player, val, playerHad);

                    if (score > bestScore) {
                        bestScore = score;
                        bestVal   = val;
                    }
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) break; // Prune sibling branches
                }
            }
        }
        return bestVal;
    }

    /**
     * Core recursive minimax.
     *
     * @param isMaximizing  true  → computer's turn (maximise score)
     *                      false → player's turn  (minimise score)
     */
    private int minimax(BingoBoard computer, BingoBoard player,
                        int depth, boolean isMaximizing,
                        int alpha, int beta) {

        // ── Terminal checks (win states) ───────────────────────────────────────
        if (completedLines(computer) >= computer.size) return  10_000_000;
        if (completedLines(player)   >= player.size)   return -10_000_000;
        if (depth == 0)                                return heuristic(computer, player);

        // ── Memoization lookup ─────────────────────────────────────────────────
        String key = stateKey(computer) + (isMaximizing ? 'M' : 'm');
        if (memo.containsKey(key)) return memo.get(key);

        int result;

        if (isMaximizing) {
            // Computer picks a number to maximise its advantage
            result = Integer.MIN_VALUE;
            outer:
            for (int i = 0; i < computer.size; i++) {
                for (int j = 0; j < computer.size; j++) {
                    if (!computer.nodes[i][j].marked) {
                        int val = computer.nodes[i][j].value;

                        // ── SIMULATE ──────────────────────────────
                        computer.nodes[i][j].marked = true;
                        boolean pHad = markByValue(player, val);

                        int score = minimax(computer, player, depth - 1, false, alpha, beta);

                        // ── BACKTRACK ─────────────────────────────
                        computer.nodes[i][j].marked = false;
                        unmarkByValue(player, val, pHad);

                        result = Math.max(result, score);
                        alpha  = Math.max(alpha,  result);
                        if (beta <= alpha) break outer; // α-β cut (MAX)
                    }
                }
            }
        } else {
            // Player picks a number to minimise computer's advantage
            result = Integer.MAX_VALUE;
            outer:
            for (int i = 0; i < player.size; i++) {
                for (int j = 0; j < player.size; j++) {
                    if (!player.nodes[i][j].marked) {
                        int val = player.nodes[i][j].value;

                        // ── SIMULATE ──────────────────────────────
                        boolean cHad = markByValue(computer, val);
                        player.nodes[i][j].marked = true;

                        int score = minimax(computer, player, depth - 1, true, alpha, beta);

                        // ── BACKTRACK ─────────────────────────────
                        unmarkByValue(computer, val, cHad);
                        player.nodes[i][j].marked = false;

                        result = Math.min(result, score);
                        beta   = Math.min(beta,   result);
                        if (beta <= alpha) break outer; // α-β cut (MIN)
                    }
                }
            }
        }

        memo.put(key, result);
        return result;
    }

    // ── Board helpers ────────────────────────────────────────────────────────

    /** Net heuristic: computer score minus player score. */
    private int heuristic(BingoBoard computer, BingoBoard player) {
        return boardScore(computer) - boardScore(player);
    }

    /** Total positional score for a board (sum across all lines). */
    private int boardScore(BingoBoard b) {
        int score = 0;
        int n = b.size;
        for (int i = 0; i < n; i++) score += lineScore(b, i, true);   // rows
        for (int i = 0; i < n; i++) score += lineScore(b, i, false);  // cols
        score += diagScore(b, true);   // main diagonal
        score += diagScore(b, false);  // anti-diagonal
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
            int j = main ? i : (b.size - 1 - i);
            if (b.nodes[i][j].marked) cnt++;
        }
        if (cnt == b.size) return 1_000_000;
        return (int) Math.pow(10, cnt);
    }

    /** Count completed lines (rows + cols + diagonals) on a board. */
    private int completedLines(BingoBoard b) {
        int lines = 0;
        for (int i = 0; i < b.size; i++) {
            if (isLineFull(b, i, true))  lines++; // row
            if (isLineFull(b, i, false)) lines++; // col
        }
        if (isDiagFull(b, true))  lines++;
        if (isDiagFull(b, false)) lines++;
        return lines;
    }

    private boolean isLineFull(BingoBoard b, int idx, boolean isRow) {
        for (int k = 0; k < b.size; k++) {
            boolean m = isRow ? b.nodes[idx][k].marked : b.nodes[k][idx].marked;
            if (!m) return false;
        }
        return true;
    }

    private boolean isDiagFull(BingoBoard b, boolean main) {
        for (int i = 0; i < b.size; i++) {
            int j = main ? i : (b.size - 1 - i);
            if (!b.nodes[i][j].marked) return false;
        }
        return true;
    }

    /**
     * Mark the node with the given value on board b.
     * @return true if we actually marked it (it was unmarked), false if already marked.
     */
    private boolean markByValue(BingoBoard b, int val) {
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                if (b.nodes[i][j].value == val && !b.nodes[i][j].marked) {
                    b.nodes[i][j].marked = true;
                    return true;
                }
        return false;
    }

    /** Undo a simulated mark (backtrack). Only unmarks if wasMarked=true. */
    private void unmarkByValue(BingoBoard b, int val, boolean wasMarked) {
        if (!wasMarked) return;
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                if (b.nodes[i][j].value == val) {
                    b.nodes[i][j].marked = false;
                    return;
                }
    }

    /**
     * Generate a compact state key from the computer board's marked pattern.
     * Since both boards always have the same set of called values (they're in sync),
     * one board's pattern uniquely identifies the full game state.
     */
    private String stateKey(BingoBoard b) {
        StringBuilder sb = new StringBuilder(b.size * b.size);
        for (int i = 0; i < b.size; i++)
            for (int j = 0; j < b.size; j++)
                sb.append(b.nodes[i][j].marked ? '1' : '0');
        return sb.toString();
    }
}
