package Bingo;
import javax.swing.*;
import java.awt.*;
public class BingoGUI extends JFrame{
	JButton[][] buttons= new JButton[5][5];
	char[] letters= {'B','I','N','G','O'};
	BingoBoard board;
	public BingoGUI(BingoBoard board) {
		this.board=board;
		setTitle("Bingo Game:");
		setSize(400,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(6,6));
		buildUI();
		setVisible(true);
		
	}
	private void buildUI() {
		add(new JLabel(""));
		for (char c : letters) {
		add(new JLabel(String.valueOf(c), SwingConstants.CENTER));
		}


		for (int i = 0; i < 5; i++) {
		  add(new JLabel(String.valueOf(letters[i]), SwingConstants.CENTER));
		  for (int j = 0; j < 5; j++) {
		    int num = board.board[i][j];
		    JButton btn = new JButton(String.valueOf(num));
		    buttons[i][j] = btn;


		    btn.addActionListener(e -> {
		    btn.setBackground(Color.GREEN);
		    btn.setEnabled(false);
		    });
		    add(btn);
		  }
		}
	}


		
		

}
