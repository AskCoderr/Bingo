package Bingo;

public class DPMoveStrategy {

    /**
     * @param computer The AI's board
     * @param player   The User's board
     * @return The best number to call
     */
    public int findBestMove(BingoBoard computer, BingoBoard player) {
        int size = computer.size;
        
        // --- MANUAL DIFFICULTY TUNING ---
        // Increase OFFENSE to make the AI focus on its own win.
        // Increase DEFENSE to make the AI block the player more aggressively.
        double OFFENSE_WEIGHT = 1.0; 
        double DEFENSE_WEIGHT = 0.8; 
        // --------------------------------

        int[] compMemo = new int[2 * size + 2];
        int[] playerMemo = new int[2 * size + 2];

        // 1. Initial State Memoization - O(N^2)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (computer.nodes[i][j].marked) updateMemo(compMemo, i, j, size);
                if (player.nodes[i][j].marked) updateMemo(playerMemo, i, j, size);
            }
        }

        int bestVal = -1;
        double maxScore = Double.NEGATIVE_INFINITY;

        // 2. Selection Loop - O(N^2)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Only consider numbers the AI hasn't marked yet
                if (!computer.nodes[i][j].marked) {
                    int val = computer.nodes[i][j].value;

                    // DP Lookups (O(1)) - Get pre-calculated line strengths
                    int compGain = getPotentialGain(compMemo, i, j, size);
                    int playerGain = getPotentialGain(playerMemo, i, j, size);

                    // THE FORMULA: High score = Best Move
                    double totalScore = (OFFENSE_WEIGHT * compGain) + (DEFENSE_WEIGHT * playerGain);

                    if (totalScore > maxScore) {
                        maxScore = totalScore;
                        bestVal = val;
                    }
                }
            }
        }
        return bestVal;
    }

    private void updateMemo(int[] memo, int r, int c, int size) {
        memo[r]++; // Row
        memo[size + c]++; // Column
        if (r == c) memo[2 * size]++; // Main Diagonal
        if (r + c == size - 1) memo[2 * size + 1]++; // Anti-Diagonal
    }

    private int getPotentialGain(int[] memo, int r, int c, int size) {
        int gain = 0;
        // Exponential scoring: 10^count means 4-in-a-row is MUCH better than 3-in-a-row
        gain += (int) Math.pow(10, memo[r]);
        gain += (int) Math.pow(10, memo[size + c]);
        if (r == c) gain += (int) Math.pow(10, memo[2 * size]);
        if (r + c == size - 1) gain += (int) Math.pow(10, memo[2 * size + 1]);
        return gain;
    }
}
