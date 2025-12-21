package Bingo;

public class GameState {
    private int a;
    private int b;

    public void playerTurn() {
        a = 1;
        b = 0;
    }

    public void computerTurn() {
        a = 0;
        b = 1;
    }

    public boolean isPlayerTurn() {
        return this.a == 1;
    }

    public boolean isComputerTurn() {
        return this.b == 1;
    }
}
