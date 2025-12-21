package Bingo;

import javax.swing.JOptionPane;

public class BingoGameController {
    GameState gameState;
    BingoBoardAnalyser bingoBoardAnalyser;
    BingoGUI bingoGUI;

    public BingoGameController(BingoBoard board, BingoBoard compBoard, GameState gameState, BingoGUI bingoGUI) {
        this.bingoBoardAnalyser = new BingoBoardAnalyser(board, compBoard, gameState);
        this.gameState = gameState;
        this.bingoGUI = bingoGUI;
    }

    public void setGameState() {
        String[] options = { "Player", "Computer" };

        int choice = JOptionPane.showOptionDialog(
                null,
                "Who should start the game?",
                "Start Game",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            gameState.playerTurn();
        } else {
            gameState.computerTurn();
        }
    }

    public void nextStep() {
        if (gameState.isPlayerTurn()) {
            startPlayerTurn();
        } else if (gameState.isComputerTurn()) {
            startComputerTurn();
        }
    }

    private void startPlayerTurn() {
        bingoGUI.setInputEnabled(true);

        bingoGUI.setOnPlayerMoveCallback(() -> {
            bingoGUI.setInputEnabled(false);

            // player has played exactly ONE move here

            if (bingoBoardAnalyser.evaluateBoard()) {
                endGame();
                return;
            }

            gameState.computerTurn();
            nextStep(); // advance state
        });
    }

    private void startComputerTurn() {
        bingoGUI.setInputEnabled(false);

        bingoBoardAnalyser.makeNextMove();

        if (bingoBoardAnalyser.evaluateBoard()) {
            endGame();
            return;
        }

        gameState.playerTurn();

        nextStep(); // advance state
    }

    private void endGame() {
        bingoGUI.setInputEnabled(false);
        JOptionPane.showMessageDialog(null, "Game Over!");
    }

}
