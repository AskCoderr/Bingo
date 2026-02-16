package Bingo;

import javax.swing.*;
import java.awt.*;

public class BingoGUI extends JFrame {
    boolean[] completedRows;
    boolean[] completedCols;
    boolean diag1Done = false;
    boolean diag2Done = false;

    JPanel boardPanel; // we will draw on this

    BingoBoard board;
    BingoBoardAnalyser analyser;
    JLabel statusLabel;

    JLabel[] topLetters;
    JLabel[] sideLetters;

    static final String[] ALL = { "B", "I", "N", "G", "O" };

    public BingoGUI(BingoBoard board) {
        this.board = board;

        setTitle("Bingo");
        setLayout(new BorderLayout(10, 10));
        topLetters = new JLabel[board.size];
        sideLetters = new JLabel[board.size];
        completedRows = new boolean[board.size];
        completedCols = new boolean[board.size];

        statusLabel = new JLabel("PLAYER TURN", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.BLUE);
        add(statusLabel, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(board.size + 1, board.size + 1, 5, 5)) {

            @Override
            public void paint(Graphics g) {
                super.paint(g); // paint children first

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(6));
                g2.setColor(Color.BLACK);

                for (int i = 0; i < board.size; i++) {

                    if (completedRows[i]) {
                        JButton left = board.buttons[i][0];
                        JButton right = board.buttons[i][board.size - 1];

                        int y = left.getY() + left.getHeight() / 2;
                        int x1 = left.getX();
                        int x2 = right.getX() + right.getWidth();

                        g2.drawLine(x1, y, x2, y);
                    }

                    if (completedCols[i]) {
                        JButton top = board.buttons[0][i];
                        JButton bottom = board.buttons[board.size - 1][i];

                        int x = top.getX() + top.getWidth() / 2;
                        int y1 = top.getY();
                        int y2 = bottom.getY() + bottom.getHeight();

                        g2.drawLine(x, y1, x, y2);
                    }
                }

                if (diag1Done) {
                    JButton start = board.buttons[0][0];
                    JButton end = board.buttons[board.size - 1][board.size - 1];

                    g2.drawLine(start.getX(), start.getY(),
                            end.getX() + end.getWidth(),
                            end.getY() + end.getHeight());
                }

                if (diag2Done) {
                    JButton start = board.buttons[0][board.size - 1];
                    JButton end = board.buttons[board.size - 1][0];

                    g2.drawLine(start.getX() + start.getWidth(), start.getY(),
                            end.getX(),
                            end.getY() + end.getHeight());
                }
            }

        };

        // Empty top-left corner
        boardPanel.add(new JLabel(""));

        // Top letters
        // Top letters
        for (int i = 0; i < board.size; i++) {
            JLabel label = new JLabel(ALL[i], SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 28));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            topLetters[i] = label; // IMPORTANT LINE

            boardPanel.add(label);
        }

        // Board rows
        for (int i = 0; i < board.size; i++) {

            // Side letter
            JLabel side = new JLabel(ALL[i], SwingConstants.CENTER);
            side.setFont(new Font("Arial", Font.BOLD, 28));
            side.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            sideLetters[i] = side; // IMPORTANT LINE

            boardPanel.add(side);

            for (int j = 0; j < board.size; j++) {
                JButton btn = new JButton(String.valueOf(board.board[i][j]));
                btn.setFont(new Font("Arial", Font.BOLD, 18));
                btn.setBackground(Color.WHITE);
                btn.setOpaque(true);
                btn.setBorderPainted(false);

                board.buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        setSize(130 * board.size, 150 * board.size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
        if (idx < 0 || idx >= board.size)
            return;

        topLetters[idx].setText("✖");
        sideLetters[idx].setText("✖");
    }

    public void disableBoard() {
        for (int i = 0; i < board.size; i++)
            for (int j = 0; j < board.size; j++)
                board.buttons[i][j].setEnabled(false);
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }

    public void markRowDone(int r) {
        completedRows[r] = true;
        boardPanel.repaint();
    }

    public void markColDone(int c) {
        completedCols[c] = true;
        boardPanel.repaint();
    }

    public void markDiag1Done() {
        diag1Done = true;
        boardPanel.repaint();
    }

    public void markDiag2Done() {
        diag2Done = true;
        boardPanel.repaint();
    }

}
