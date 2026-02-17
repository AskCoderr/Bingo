package Bingo;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class BingoBoardAnalyser {

    BingoBoard player, computer;
    BingoGUI playerGUI, computerGUI;

    boolean[] pRow, pCol, pDiag;
    boolean[] cRow, cCol, cDiag;

    int playerLines = 0;
    int computerLines = 0;
    boolean gameOver = false;

    Random rand = new Random();
    
    // AI STRATEGY (Dynamic Programming)
    DPMoveStrategy ai = new DPMoveStrategy();

    public BingoBoardAnalyser(BingoBoard p, BingoBoard c,
            BingoGUI pg, BingoGUI cg) {
        player = p;
        computer = c;
        playerGUI = pg;
        computerGUI = cg;

        int n = p.size;
        pRow = new boolean[n];
        pCol = new boolean[n];
        pDiag = new boolean[2];

        cRow = new boolean[n];
        cCol = new boolean[n];
        cDiag = new boolean[2];
    }

    public void playerMove(int val) {

        if (gameOver)
            return;

        // PLAYER MOVE
        playerGUI.setStatus("PLAYER TURN");
        computerGUI.setStatus("PLAYER TURN");

        mark(player, val, Color.GREEN);
        mark(computer, val, Color.GREEN);

        updateLines(player, pRow, pCol, pDiag, true);

        if (playerLines >= player.size) {
            endGame("PLAYER WINS!");
            return;
        }

        // COMPUTER THINKING
        playerGUI.setStatus("COMPUTER THINKING...");
        computerGUI.setStatus("COMPUTER THINKING...");

        javax.swing.Timer timer = new javax.swing.Timer(800, e -> {


            // DP & MEMOIZATION: Use intelligent strategy
            int compVal = ai.findBestMove(computer, player);
            if (compVal == -1)
                return;

            // COMPUTER MOVE
            mark(player, compVal, Color.RED);
            mark(computer, compVal, Color.RED);

            updateLines(computer, cRow, cCol, cDiag, false);

            if (computerLines >= computer.size) {
                endGame("COMPUTER WINS!");
                return;
            }

            // BACK TO PLAYER
            if (!gameOver) {
                playerGUI.setStatus("PLAYER TURN");
                computerGUI.setStatus("PLAYER TURN");
            }

        });

        timer.setRepeats(false);
        timer.start();
    }

    private void mark(BingoBoard b, int val, Color c) {
        // LINEAR SEARCH
        for (int i = 0; i < b.size; i++) {
            for (int j = 0; j < b.size; j++) {
                if (b.nodes[i][j].value == val && !b.nodes[i][j].marked) {
                    b.nodes[i][j].marked = true;
                    b.buttons[i][j].setBackground(c);
                    b.buttons[i][j].setEnabled(false);
                    return;
                }
            }
        }
    }

    private void updateLines(BingoBoard b,
            boolean[] rowDone,
            boolean[] colDone,
            boolean[] diagDone,
            boolean isPlayer) {

        int n = b.size;

        for (int i = 0; i < n; i++) {

            if (!rowDone[i] && fullRow(b, i)) {
                rowDone[i] = true;
                cross(isPlayer);

                if (isPlayer)
                    playerGUI.markRowDone(i);
                else
                    computerGUI.markRowDone(i);
            }

            if (!colDone[i] && fullCol(b, i)) {
                colDone[i] = true;
                cross(isPlayer);

                if (isPlayer)
                    playerGUI.markColDone(i);
                else
                    computerGUI.markColDone(i);
            }
        }

        if (!diagDone[0] && fullDiag1(b)) {
            diagDone[0] = true;
            cross(isPlayer);

            if (isPlayer)
                playerGUI.markDiag1Done();
            else
                computerGUI.markDiag1Done();
        }

        if (!diagDone[1] && fullDiag2(b)) {
            diagDone[1] = true;
            cross(isPlayer);

            if (isPlayer)
                playerGUI.markDiag2Done();
            else
                computerGUI.markDiag2Done();
        }
    }

    private void cross(boolean isPlayer) {
        if (isPlayer) {
            if (playerLines < player.size)
                playerGUI.crossLetter(playerLines++);
        } else {
            if (computerLines < computer.size)
                computerGUI.crossLetter(computerLines++);
        }
    }

    // DIVIDE AND CONQUER: Recursive Line Checks
    // Recurrence Relation: T(n) = 2T(n/2) + O(1)
    
    // Helper function for Divide and Conquer
    private boolean checkRange(Node[] line, int start, int end) {
        // Divide Step: Check range bounds/base case
        if (start == end) {
            return line[start].marked;
        }
        
        // Conquer Step: Recursively check left and right halves
        int mid = (start + end) / 2;
        boolean left = checkRange(line, start, mid);
        boolean right = checkRange(line, mid + 1, end);
        
        // Combine Step: Logical AND
        return left && right;
    }

    private boolean fullRow(BingoBoard b, int r) {
        return checkRange(b.nodes[r], 0, b.size - 1);
    }
    
    private boolean fullCol(BingoBoard b, int c) {
        // Need to construct a temporary array/view for column to use generic helper
        // Or implement specific helper. Implementing specific helper to avoid O(n) copy.
        return checkColDnC(b.nodes, c, 0, b.size - 1);
    }

    private boolean checkColDnC(Node[][] nodes, int col, int start, int end) {
        if (start == end) return nodes[start][col].marked;
        int mid = (start + end) / 2;
        return checkColDnC(nodes, col, start, mid) && 
               checkColDnC(nodes, col, mid + 1, end);
    }

    private boolean fullDiag1(BingoBoard b) {
        return checkDiag1DnC(b.nodes, 0, b.size - 1);
    }
    
    private boolean checkDiag1DnC(Node[][] nodes, int start, int end) {
        if (start == end) return nodes[start][start].marked;
        int mid = (start + end) / 2;
        return checkDiag1DnC(nodes, start, mid) && 
               checkDiag1DnC(nodes, mid + 1, end);
    }

    private boolean fullDiag2(BingoBoard b) {
        return checkDiag2DnC(b.nodes, 0, b.size - 1, b.size);
    }
    
    private boolean checkDiag2DnC(Node[][] nodes, int start, int end, int size) {
        if (start == end) return nodes[start][size - 1 - start].marked;
        int mid = (start + end) / 2;
        return checkDiag2DnC(nodes, start, mid, size) && 
               checkDiag2DnC(nodes, mid + 1, end, size);
    }

    private int computerPick() {
        int tries = 100;
        while (tries-- > 0) {
            int i = rand.nextInt(computer.size);
            int j = rand.nextInt(computer.size);
            if (!computer.nodes[i][j].marked)
                return computer.nodes[i][j].value;
        }
        return -1;
    }

    private void endGame(String msg) {
        gameOver = true;
        playerGUI.disableBoard();
        computerGUI.disableBoard();
        JOptionPane.showMessageDialog(null, msg);
    }
}
