package Bingo;

import javax.swing.JButton;

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

        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = num++;
                nodes[i][j] = new Node(board[i][j]);
            }
        }
    }
}

