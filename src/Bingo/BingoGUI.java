package Bingo;

import javax.swing.*;
import java.awt.*;

public class BingoGUI extends JFrame {
    boolean[] completedRows;
    boolean[] completedCols;
    boolean diag1Done = false;
    boolean diag2Done = false;

    JPanel boardPanel;

    BingoBoard board;
    BingoBoardAnalyser analyser;
    JLabel statusLabel;
    JLabel linesLabel;

    JLabel[] topLetters;
    JLabel[] sideLetters;

    static final String[] ALL = { "B", "I", "N", "G", "O" };

    public BingoGUI(BingoBoard board) {
        this.board = board;

        setTitle("Bingo");
        setLayout(new BorderLayout(10, 10));
        topLetters   = new JLabel[board.size];
        sideLetters  = new JLabel[board.size];
        completedRows = new boolean[board.size];
        completedCols = new boolean[board.size];

        // ── Status bar (top) ───────────────────────────────────────────────
        statusLabel = new JLabel("PLAYER TURN", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(70, 130, 180)); // steel blue
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(statusLabel, BorderLayout.NORTH);

        // ── Board panel (centre) ───────────────────────────────────────────
        boardPanel = new JPanel(new GridLayout(board.size + 1, board.size + 1, 5, 5)) {

            @Override
            public void paint(Graphics g) {
                super.paint(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(30, 30, 30));

                for (int i = 0; i < board.size; i++) {

                    if (completedRows[i]) {
                        JButton left  = board.buttons[i][0];
                        JButton right = board.buttons[i][board.size - 1];

                        int y  = left.getY()  + left.getHeight()  / 2;
                        int x1 = left.getX();
                        int x2 = right.getX() + right.getWidth();

                        g2.drawLine(x1, y, x2, y);
                    }

                    if (completedCols[i]) {
                        JButton top    = board.buttons[0][i];
                        JButton bottom = board.buttons[board.size - 1][i];

                        int x  = top.getX()    + top.getWidth()    / 2;
                        int y1 = top.getY();
                        int y2 = bottom.getY() + bottom.getHeight();

                        g2.drawLine(x, y1, x, y2);
                    }
                }

                if (diag1Done) {
                    JButton start = board.buttons[0][0];
                    JButton end   = board.buttons[board.size - 1][board.size - 1];

                    g2.drawLine(start.getX(), start.getY(),
                            end.getX() + end.getWidth(),
                            end.getY() + end.getHeight());
                }

                if (diag2Done) {
                    JButton start = board.buttons[0][board.size - 1];
                    JButton end   = board.buttons[board.size - 1][0];

                    g2.drawLine(start.getX() + start.getWidth(), start.getY(),
                            end.getX(),
                            end.getY() + end.getHeight());
                }
            }

        };

        boardPanel.setBackground(new Color(245, 245, 245));

        // Empty top-left corner
        boardPanel.add(new JLabel(""));

        // Top letters (B I N G O)
        for (int i = 0; i < board.size; i++) {
            JLabel label = new JLabel(ALL[i], SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 28));
            label.setForeground(new Color(70, 130, 180));
            label.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

            topLetters[i] = label;
            boardPanel.add(label);
        }

        // Board rows
        for (int i = 0; i < board.size; i++) {

            // Side letter
            JLabel side = new JLabel(ALL[i], SwingConstants.CENTER);
            side.setFont(new Font("Arial", Font.BOLD, 28));
            side.setForeground(new Color(70, 130, 180));
            side.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

            sideLetters[i] = side;
            boardPanel.add(side);

            for (int j = 0; j < board.size; j++) {
                JButton btn = new JButton(String.valueOf(board.board[i][j]));
                btn.setFont(new Font("Arial", Font.BOLD, 18));
                btn.setBackground(new Color(255, 255, 255));
                btn.setForeground(new Color(40, 40, 40));
                btn.setOpaque(true);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                board.buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        // ── Lines counter (bottom) ─────────────────────────────────────────
        linesLabel = new JLabel("Lines: 0 / " + board.size, SwingConstants.CENTER);
        linesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        linesLabel.setForeground(new Color(60, 60, 60));
        linesLabel.setOpaque(true);
        linesLabel.setBackground(new Color(235, 235, 235));
        linesLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        add(linesLabel, BorderLayout.SOUTH);

        setSize(130 * board.size, 155 * board.size);
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

    // ── Public update methods ─────────────────────────────────────────────

    /** Update the "Lines: X / N" counter at the bottom. */
    public void updateLinesCount(int lines) {
        linesLabel.setText("Lines: " + lines + " / " + board.size);
    }

    /**
     * Flash all buttons with a win/lose colour (8 alternating frames × 150ms).
     * Called when the game ends to give visual feedback before the dialog appears.
     */
    public void flashResult(Color flashColor) {
        javax.swing.Timer flashTimer = new javax.swing.Timer(150, null);
        final int[] frame = { 0 };
        flashTimer.addActionListener(e -> {
            boolean bright = (frame[0] % 2 == 0);
            for (int i = 0; i < board.size; i++)
                for (int j = 0; j < board.size; j++)
                    board.buttons[i][j].setBackground(bright ? flashColor : Color.WHITE);
            frame[0]++;
            if (frame[0] >= 8) flashTimer.stop();
        });
        flashTimer.start();
    }

    public void crossLetter(int idx) {
        if (idx < 0 || idx >= board.size)
            return;

        topLetters[idx].setText("✖");
        topLetters[idx].setForeground(new Color(220, 50, 50));
        sideLetters[idx].setText("✖");
        sideLetters[idx].setForeground(new Color(220, 50, 50));
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
