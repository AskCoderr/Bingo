package Bingo;

public class Main {
    public static void main(String[] args) {
        // 1. Create the data models
        BingoBoard playerBoard = new BingoBoard();
        BingoBoard computerBoard = new BingoBoard();

        // 2. Create the Player UI
        BingoGUI playerGUI = new BingoGUI(playerBoard);
        playerGUI.setTitle("Player Board");

        // 3. Create the Computer UI
        BingoGUI computerGUI = new BingoGUI(computerBoard);
        computerGUI.setTitle("Computer Board");
        computerGUI.setLocation(450, 0); 

        // 4. Initialize State and Controller
        GameState state = new GameState();
        BingoGameController controller = new BingoGameController(playerBoard, computerBoard, state, playerGUI, computerGUI);

        // 5. Start the Game
        controller.setGameState();
        controller.nextStep();
    }
}