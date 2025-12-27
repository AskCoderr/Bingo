package Bingo;

import javax.swing.*;
import java.awt.*;

public class BingoGUI extends JFrame {

    BingoBoard board;
    BingoBoardAnalyser analyser;

    JLabel[] topLetters;
    JLabel[] sideLetters;

    static final String[] ALL = {"B", "I", "N", "G", "O"};

    public BingoGUI(BingoBoard board) {
        this.board = board;

        setTitle("Bingo");
        setLayout(new BorderLayout(10, 10));

        add(createTopLetters(), BorderLayout.NORTH);
        add(createSideLetters(), BorderLayout.WEST);
        add(createBoard(), BorderLayout.CENTER);

        setSize(130 * board.size, 130 * board.size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createTopLetters() {
        JPanel panel = new JPanel(new GridLayout(1, board.size));
        topLetters = new JLabel[board.size];

        for (int i = 0; i < board.size; i++) {
            topLetters[i] = new JLabel(ALL[i], SwingConstants.CENTER);
            topLetters[i].setFont(new Font("Arial", Font.BOLD, 28));
            topLetters[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(topLetters[i]);
        }
        return panel;
    }

    private JPanel createSideLetters() {
        JPanel panel = new JPanel(new GridLayout(board.size, 1));
        sideLetters = new JLabel[board.size];

        for (int i = 0; i < board.size; i++) {
            sideLetters[i] = new JLabel(ALL[i], SwingConstants.CENTER);
            sideLetters[i].setFont(new Font("Arial", Font.BOLD, 28));
            sideLetters[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(sideLetters[i]);
        }
        return panel;
    }

    private JPanel createBoard() {
        JPanel panel = new JPanel(new GridLayout(board.size, board.size, 5, 5));

        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                JButton btn = new JButton(String.valueOf(board.board[i][j]));
                btn.setFont(new Font("Arial", Font.BOLD, 18));
                board.buttons[i][j] = btn;
                panel.add(btn);
            }
        }
        return panel;
    }

    public void setAnalyser(BingoBoardAnalyser analyser) {
        this.analyser = analyser;

        for (int i = 0; i < board.size; i++) {
            for (int j = 0; j < board.size; j++) {
                JButton btn = board.buttons[i][j];
                int val = board.board[i][j];

                btn.addActionListener(e -> {
                    if (this.analyser != null)
                        this.analyser.playerMove(val);
                });
            }
        }
    }

    public void crossLetter(int idx) {
        if (idx < 0 || idx >= board.size) return;

        topLetters[idx].setText("✖");
        sideLetters[idx].setText("✖");
    }

    public void disableBoard() {
        for (int i = 0; i < board.size; i++)
            for (int j = 0; j < board.size; j++)
                board.buttons[i][j].setEnabled(false);
    }
}

