package Bingo;

import javax.swing.JButton;
import java.util.ArrayList;
import java.util.Collections;

public class BingoBoard {
    int size;
    public JButton[][] buttons;
    public int[][] board;
    public Node[][] nodes;

    public BingoBoard(int size) {
        this.size = size;
        buttons = new JButton[size][size];
        board = new int[size][size];
        nodes = new Node[size][size];

        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 1; i <= size * size; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = numbers.get(index++);
                nodes[i][j] = new Node(board[i][j]);
            }
        }

    }
}
