package Bingo;

import javax.swing.JOptionPane;

public class BingoGameController {
    GameState gameState;
    BingoBoardAnalyser bingoBoardAnalyser;
    BingoGUI playerGUI; // Renamed for clarity
    BingoGUI computerGUI; // Added to manage the computer's interface

    // Updated constructor to accept both GUIs
    public BingoGameController(BingoBoard board, BingoBoard compBoard, GameState gameState, BingoGUI playerGUI, BingoGUI computerGUI) {
        this.bingoBoardAnalyser = new BingoBoardAnalyser(board, compBoard, gameState);
        this.gameState = gameState;
        this.playerGUI = playerGUI;
        this.computerGUI = computerGUI;
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
            gameState.playerTurn(); //
        } else {
            gameState.computerTurn(); //
        }
    }

    public void nextStep() {
        if (gameState.isPlayerTurn()) { //
            startPlayerTurn();
        } else if (gameState.isComputerTurn()) { //
            startComputerTurn();
        }
    }

    private void startPlayerTurn() {
        playerGUI.setInputEnabled(true); // Enable player interaction
        computerGUI.setInputEnabled(false); // Ensure computer board is non-interactive

        playerGUI.setOnPlayerMoveCallback(() -> {
            playerGUI.setInputEnabled(false);

            int move = playerGUI.getSelectedMove(); 
            bingoBoardAnalyser.applyMoveToBoth(move);

            // Logic to check win condition after player move
            if (bingoBoardAnalyser.evaluateBoard()) { //
                endGame();
                return;
            }

            gameState.computerTurn(); //
            nextStep(); 
        });
    }

    private void startComputerTurn() {
        playerGUI.setInputEnabled(false);
        computerGUI.setInputEnabled(false); // Computer board stays disabled for user input

        bingoBoardAnalyser.makeNextMove(); //

        // Logic to check win condition after computer move
        if (bingoBoardAnalyser.evaluateBoard()) { //
            endGame();
            return;
        }

        gameState.playerTurn(); //
        nextStep(); 
    }

    private void endGame() {
        playerGUI.setInputEnabled(false); //
        computerGUI.setInputEnabled(false); // Lock the computer board at game end
        JOptionPane.showMessageDialog(null, "Game Over!");
    }
}