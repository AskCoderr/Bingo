package Bingo;

public class Node {
    int value;
    boolean marked;
    int row, col;

    public Node(int value, int r, int c) {
        this.value = value;
        this.marked = false;
        this.row = r;
        this.col = c;
    }
}

