package Bingo;

public class GameState {
    private boolean playerTurn = true;

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void toggleTurn() {
        playerTurn = !playerTurn;
    }
}

