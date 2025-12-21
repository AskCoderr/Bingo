package Bingo;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {
		BingoBoard board = new BingoBoard();
		BingoBoard compBoard = new BingoBoard();
		GameState gameState = new GameState();
		BingoGUI bingoGUI = new BingoGUI(board);

		BingoGameController bingoGameController = new BingoGameController(board, compBoard, gameState, bingoGUI);
		bingoGameController.setGameState();
		bingoGameController.nextStep();
	}
}
