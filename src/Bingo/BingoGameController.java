package Bingo;

public class BingoGameController {

    BingoBoard playerBoard, computerBoard;
    BingoGUI playerGUI, computerGUI;
    BingoBoardAnalyser analyser;

    public BingoGameController(BingoBoard playerBoard,
                               BingoBoard computerBoard,
                               BingoGUI playerGUI,
                               BingoGUI computerGUI) {

        this.playerBoard = playerBoard;
        this.computerBoard = computerBoard;
        this.playerGUI = playerGUI;
        this.computerGUI = computerGUI;

        this.analyser = new BingoBoardAnalyser(
                playerBoard, computerBoard,
                playerGUI, computerGUI
        );

        playerGUI.setAnalyser(analyser);
        computerGUI.setAnalyser(analyser);
    }

    public void startGame() {
        System.out.println("Game started");
    }
}
