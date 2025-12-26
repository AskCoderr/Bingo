package Bingo;

import java.awt.Color;

public class BingoBoardAnalyser {
    BingoBoard board;
    BingoBoard compBoard;
    GameState gameState;
    GreedyMoveStrategy strategy = new GreedyMoveStrategy();

    public BingoBoardAnalyser(BingoBoard board, BingoBoard compBoard, GameState gameState) {
        this.board = board;
        this.compBoard = compBoard;
        this.gameState = gameState;
    }

    public void makeNextMove() {
        // analyze the current board and make the best move
        int move = strategy.findBestMove(compBoard);
        System.out.println("Computer chooses: "+move);
        applyMoveToBoth(move);
    }

    public void applyMoveToBoth(int value){
        markValueOnBoard(board,value);
        markValueOnBoard(compBoard, value);
    }

    private void markValueOnBoard(BingoBoard b, int val){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                Node node = b.nodes[i][j];

                if(node.value == val){
                    node.marked = true;

                    if(b.buttons != null && b.buttons[i][j] != null){
                        b.buttons[i][j].setBackground(Color.GREEN);
                        b.buttons[i][j].setEnabled(false);
                    }
                    return;
                }
            }
        }
    }

    public boolean evaluateBoard() {
        // evaluate if the current player has won the game
        BingoBoard current = gameState.isPlayerTurn() ? board : compBoard;
        int completedLines = 0;

        for(int i=0; i<5; i++){
            if(checkRow(current,i)){
                completedLines++;
            }
            if(checkColumn(current,i)){
                completedLines++;
            }
        }

        if(checkMainDiagonal(current)) completedLines++;
        if(checkCrossDiagonal(current)) completedLines++;
        
        if(completedLines >= 5){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean checkRow(BingoBoard b, int row){
        for(int k=0; k<5; k++){
            if(!b.nodes[row][k].marked) return false;
        }
        return true;
    }

    private boolean checkColumn(BingoBoard b, int col) {
        for (int row = 0; row < 5; row++) {
            if (!b.nodes[row][col].marked) return false;
        }
        return true;
    }

    private boolean checkMainDiagonal(BingoBoard b) {
        for (int i = 0; i < 5; i++) {
            if (!b.nodes[i][i].marked) return false;
        }
        return true;
    }

    private boolean checkCrossDiagonal(BingoBoard b) {
        for (int i = 0; i < 5; i++) {
            if (!b.nodes[i][4 - i].marked) return false;
        }
        return true;
    }
}
