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
        if (gameOver) return;

        mark(player, val, Color.GREEN);
        mark(computer, val, Color.RED);

        updateLines(player, pRow, pCol, pDiag, true);
        if (playerLines >= player.size) {
            endGame("PLAYER WINS!");
            return;
        }

        int compVal = computerPick();
        if (compVal == -1) return;

        mark(player, compVal, Color.GREEN);
        mark(computer, compVal, Color.RED);

        updateLines(computer, cRow, cCol, cDiag, false);
        if (computerLines >= computer.size) {
            endGame("COMPUTER WINS!");
        }
    }

    private void mark(BingoBoard b, int val, Color c) {
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
            }
            if (!colDone[i] && fullCol(b, i)) {
                colDone[i] = true;
                cross(isPlayer);
            }
        }

        if (!diagDone[0] && fullDiag1(b)) {
            diagDone[0] = true;
            cross(isPlayer);
        }
        if (!diagDone[1] && fullDiag2(b)) {
            diagDone[1] = true;
            cross(isPlayer);
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

    private boolean fullRow(BingoBoard b, int r) {
        for (int j = 0; j < b.size; j++)
            if (!b.nodes[r][j].marked) return false;
        return true;
    }

    private boolean fullCol(BingoBoard b, int c) {
        for (int i = 0; i < b.size; i++)
            if (!b.nodes[i][c].marked) return false;
        return true;
    }

    private boolean fullDiag1(BingoBoard b) {
        for (int i = 0; i < b.size; i++)
            if (!b.nodes[i][i].marked) return false;
        return true;
    }

    private boolean fullDiag2(BingoBoard b) {
        for (int i = 0; i < b.size; i++)
            if (!b.nodes[i][b.size - 1 - i].marked) return false;
        return true;
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

