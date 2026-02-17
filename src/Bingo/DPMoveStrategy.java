package Bingo;

import java.util.HashMap;
import java.util.Map;

public class DPMoveStrategy {

    // MEMOIZATION TABLE: Stores computed scores for board states
    private Map<String, Integer> memo = new HashMap<>();

    public int findBestMove(BingoBoard computer, BingoBoard player) {
        int bestVal = -1;
        int maxScore = Integer.MIN_VALUE;

        // Iterate over all possible moves (unmarked nodes)
        for (int i = 0; i < computer.size; i++) {
            for (int j = 0; j < computer.size; j++) {
                if (!computer.nodes[i][j].marked) {
                    
                    int val = computer.nodes[i][j].value;
                    
                    // Temporarily mark to evaluate state on BOTH boards
                    // Note: In a real game, 'val' is marked on both boards.
                    // We need to simulate this.
                    
                    boolean playerHadIt = markSimulated(player, val);
                    computer.nodes[i][j].marked = true;
                    
                    // DP Step: Evaluate resulting state
                    int compScore = evaluateState(computer);
                    int playerScore = evaluateState(player);
                    
                    // HEURISTIC: Net Score = My Score - (Weight * Opponent Score)
                    // Adjusted to prioritize WINNING (lower weight on defense)
                    int netScore = compScore - (int)(0.5 * playerScore);
                    
                    // Backtrack
                    computer.nodes[i][j].marked = false;
                    unmarkSimulated(player, val, playerHadIt);

                    if (netScore > maxScore) {
                        maxScore = netScore;
                        bestVal = val;
                    }
                }
            }
        }
        return bestVal;
    }
    
    private boolean markSimulated(BingoBoard b, int val) {
        for(int i=0; i<b.size; i++) {
            for(int j=0; j<b.size; j++) {
                if(b.nodes[i][j].value == val) {
                    if(!b.nodes[i][j].marked) {
                        b.nodes[i][j].marked = true;
                        return true; // We marked it
                    }
                    return false; // Already marked (shouldn't happen for valid move)
                }
            }
        }
        return false;
    }

    private void unmarkSimulated(BingoBoard b, int val, boolean actuallyMarked) {
        if(!actuallyMarked) return;
        for(int i=0; i<b.size; i++) {
            for(int j=0; j<b.size; j++) {
                if(b.nodes[i][j].value == val) {
                    b.nodes[i][j].marked = false;
                    return;
                }
            }
        }
    }

    // TOP-DOWN Check with Memoization
    private int evaluateState(BingoBoard b) {
        String stateKey = generateStateKey(b);
        
        // Memoization Lookup
        if (memo.containsKey(stateKey)) {
            return memo.get(stateKey);
        }
        
        // If not in memo, calculate score
        int score = calculateScore(b);
        
        // Store in Memo table
        memo.put(stateKey, score);
        
        return score;
    }

    private String generateStateKey(BingoBoard b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.size; i++) {
            for (int j = 0; j < b.size; j++) {
                sb.append(b.nodes[i][j].marked ? "1" : "0");
            }
        }
        return sb.toString();
    }

    // Heuristic score calculation (simulating "cost" or "value" of state)
    // Higher score = closer to winning
    private int calculateScore(BingoBoard b) {
        int score = 0;
        int size = b.size;

        // Rows
        for (int i = 0; i < size; i++) score += evaluateLine(b.nodes[i]);
        
        // Cols
        for (int i = 0; i < size; i++) {
            Node[] col = new Node[size];
            for(int k=0; k<size; k++) col[k] = b.nodes[k][i];
            score += evaluateLine(col);
        }
        
        // Diag 1
        Node[] d1 = new Node[size];
        for(int i=0; i<size; i++) d1[i] = b.nodes[i][i];
        score += evaluateLine(d1);

        // Diag 2
        Node[] d2 = new Node[size];
        for(int i=0; i<size; i++) d2[i] = b.nodes[i][size - 1 - i];
        score += evaluateLine(d2);

        return score;
    }

    private int evaluateLine(Node[] line) {
        int markedCount = 0;
        for (Node n : line) {
            if (n.marked) markedCount++;
        }
        // Exponential weighting: 4 marked is MUCH better than 3
        // WINNING LINE (Size 5) should be MASSIVE
        if (markedCount == line.length) return 1000000; 
        return (int) Math.pow(10, markedCount);
    }
}
