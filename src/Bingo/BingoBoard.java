package Bingo;

public class BingoBoard {
	public int[][] board= new int[5][5];
	public Node[][] nodes= new Node[5][5];
	public BingoBoard() {
		createBoard();
		connectNodes();
	}
	private void createBoard() {
		int num=1;
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				board[i][j]=num;
				nodes[i][j]= new Node(num++);
			}
		}
	}
	private void connectNodes() {
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				if (i > 0) nodes[i][j].neighbors.add(nodes[i - 1][j]);
				if (i < 4) nodes[i][j].neighbors.add(nodes[i + 1][j]);
				if (j > 0) nodes[i][j].neighbors.add(nodes[i][j - 1]);
				if (j < 4) nodes[i][j].neighbors.add(nodes[i][j + 1]);
				if (i > 0 && j > 0) nodes[i][j].neighbors.add(nodes[i - 1][j - 1]);
				if (i > 0 && j < 4) nodes[i][j].neighbors.add(nodes[i - 1][j + 1]);
				if (i < 4 && j > 0) nodes[i][j].neighbors.add(nodes[i + 1][j - 1]);
				if (i < 4 && j < 4) nodes[i][j].neighbors.add(nodes[i + 1][j + 1]);
			}
		}
	}
	

}
