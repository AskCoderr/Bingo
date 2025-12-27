package Bingo;

import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        String[] options = {"3x3", "4x4", "5x5"};
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Select Bingo Board Size",
                "Bingo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == null) System.exit(0);

        int size;
        if (choice.equals("3x3")) size = 3;
        else if (choice.equals("4x4")) size = 4;
        else size = 5;

        BingoBoard playerBoard = new BingoBoard(size);
        BingoBoard computerBoard = new BingoBoard(size);

        BingoGUI playerGUI = new BingoGUI(playerBoard);
        playerGUI.setTitle("Player Board");

        BingoGUI computerGUI = new BingoGUI(computerBoard);
        computerGUI.setTitle("Computer Board");
        computerGUI.setLocation(150 * size, 0);

        BingoGameController controller =
                new BingoGameController(
                        playerBoard,
                        computerBoard,
                        playerGUI,
                        computerGUI
                );

        controller.startGame();
    }
}

