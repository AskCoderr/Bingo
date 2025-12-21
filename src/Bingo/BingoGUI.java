package Bingo;

import javax.swing.*;
import java.awt.*;

public class BingoGUI extends JFrame {
	char[] letters = { 'B', 'I', 'N', 'G', 'O' };
	BingoBoard board;
	private volatile Integer selectedMove = null;
	private Runnable onPlayerMoveCallback;

	public BingoGUI(BingoBoard board) {
		this.board = board;
		setTitle("Bingo Game:");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(6, 6));
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

				final int row = i;
				final int col = j;

				int num = board.board[row][col];
				JButton btn = new JButton(String.valueOf(num));
				this.board.buttons[row][col] = btn;

				btn.addActionListener(e -> {
					btn.setBackground(Color.GREEN);

					board.nodes[row][col].marked = true;
					selectedMove = board.board[row][col];

					if (onPlayerMoveCallback != null) {
						onPlayerMoveCallback.run();
					}
				});

				add(btn);
			}
		}
	}

	public void setInputEnabled(boolean enabled) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {

				JButton btn = board.buttons[i][j];
				if (btn == null)
					continue;

				// Disable permanently if already selected
				if (board.nodes[i][j].marked) {
					btn.setEnabled(false);
				} else {
					btn.setEnabled(enabled);
				}
			}
		}
	}

	public void setOnPlayerMoveCallback(Runnable callback) {
		this.onPlayerMoveCallback = callback;
	}

}
