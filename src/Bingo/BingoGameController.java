package Bingo;

public class BingoGameController {

    BingoBoard playerBoard, computerBoard;
    BingoGUI playerGUI, computerGUI;
    BingoBoardAnalyser analyser;

    public BingoGameController(BingoBoard playerBoard,
                               BingoBoard computerBoard,
                               BingoGUI playerGUI,
                               BingoGUI computerGUI,
                               MoveStrategy strategy) {

        this.playerBoard   = playerBoard;
        this.computerBoard = computerBoard;
        this.playerGUI     = playerGUI;
        this.computerGUI   = computerGUI;

        this.analyser = new BingoBoardAnalyser(
                playerBoard, computerBoard,
                playerGUI, computerGUI,
                strategy
        );

        // Only wire the PLAYER board's buttons to fire moves.
        // The computer board is view-only (no click listeners).
        playerGUI.setAnalyser(analyser);
    }

    public void startGame() {
        System.out.println("Game started");
    }
}
