package Bingo;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        // ── Step 1: Board size ──────────────────────────────────────────────
        String[] sizes = {"3x3", "4x4", "5x5"};
        String sizeChoice = (String) JOptionPane.showInputDialog(
                null,
                "Select Bingo Board Size",
                "Bingo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                sizes,
                sizes[2]
        );

        if (sizeChoice == null) System.exit(0);

        int size;
        if      (sizeChoice.equals("3x3")) size = 3;
        else if (sizeChoice.equals("4x4")) size = 4;
        else                               size = 5;

        // ── Step 2: Difficulty ──────────────────────────────────────────────
        String[] difficulties = {"Easy", "Medium", "Hard", "Expert"};
        String difficulty = (String) JOptionPane.showInputDialog(
                null,
                "<html><b>Select Difficulty</b><br><br>"
                + "<small>"
                + "Easy   &nbsp;&nbsp;→ Random moves<br>"
                + "Medium → DP + Memoization + <b>Radix Sort</b><br>"
                + "Hard   &nbsp;&nbsp;→ <b>Branch and Bound</b><br>"
                + "Expert &nbsp;→ <b>Minimax Backtracking</b> (α-β pruning)"
                + "</small></html>",
                "Bingo – Difficulty",
                JOptionPane.QUESTION_MESSAGE,
                null,
                difficulties,
                difficulties[1]
        );

        if (difficulty == null) System.exit(0);

        MoveStrategy strategy = switch (difficulty) {
            case "Easy"   -> new RandomMoveStrategy();
            case "Hard"   -> new BranchAndBoundStrategy();
            case "Expert" -> new MinimaxStrategy();
            default       -> new DPMoveStrategy();     // Medium
        };

        // ── Step 3: Create boards ───────────────────────────────────────────
        BingoBoard playerBoard   = new BingoBoard(size);
        BingoBoard computerBoard = new BingoBoard(size);

        BingoGUI playerGUI = new BingoGUI(playerBoard);
        playerGUI.setTitle("Player Board  [" + difficulty + " | " + sizeChoice + "]");

        BingoGUI computerGUI = new BingoGUI(computerBoard);
        computerGUI.setTitle("Computer Board  [" + difficulty + " | " + sizeChoice + "]");
        computerGUI.setLocation(155 * size, 0);

        // ── Step 4: Wire everything together ───────────────────────────────
        BingoGameController controller =
                new BingoGameController(
                        playerBoard,
                        computerBoard,
                        playerGUI,
                        computerGUI,
                        strategy
                );

        controller.startGame();
    }
}
