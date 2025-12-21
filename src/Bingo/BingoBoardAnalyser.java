package Bingo;

public class BingoBoardAnalyser {
    BingoBoard board;
    BingoBoard compBoard;
    GameState gameState;

    public BingoBoardAnalyser(BingoBoard board, BingoBoard compBoard, GameState gameState) {
        this.board = board;
        this.compBoard = compBoard;
        this.gameState = gameState;
    }

    public void makeNextMove() {
        // analyze the current board and make the best move
        System.out.println("Computer played a dummy move");
    }

    public boolean evaluateBoard() {
        // evaluate if the current player has won the game
        return false;
    }
}
