package Bingo;

public class GreedyMoveStrategy {
    private final int SIZE = 5;

    public int findBestMove(BingoBoard compBoard){
        int bestScore = -1;
        int bestNumber = -1;

        for(int i=0; i<SIZE; i++){
            for(int j=0; j<SIZE; j++){
                Node currentNode = compBoard.nodes[i][j];

                if(!currentNode.marked){
                    currentNode.marked = true;
                    int score = evaluateBoard(compBoard);
                    currentNode.marked = false;

                    if(score > bestScore){
                        bestScore = score;
                        bestNumber = currentNode.value;
                    }
                }
            }
        }
        return bestNumber;
    }

    private int evaluateBoard(BingoBoard b){
        int score = 0;

        // Rows and Columns
        for(int i=0; i<SIZE; i++){
            score += countRows(b,i);
            score += countColumns(b,i);
        }

        score += countDiagonals(b);

        return score;
    }

    private int countRows(BingoBoard b, int index){
        int count = 0;

        for(int k=0; k<SIZE; k++){
            if(b.nodes[index][k].marked){
                count++;
            }
        }

        if(count == SIZE){
            return 100;
        }
        else{
            return count;
        }
    }

    private int countColumns(BingoBoard b, int index){
        int count = 0;

        for(int k=0; k<SIZE; k++){
            if(b.nodes[k][index].marked){
                count++;
            }
        }

        if(count == SIZE){
            return 100;
        }
        else{
            return count;
        }
    }

    private int countDiagonals(BingoBoard b){
        int d1=0, d2=0;
        int score;

        for(int i=0; i<SIZE; i++){
            if(b.nodes[i][i].marked) d1++;
            if(b.nodes[i][SIZE-i-1].marked) d2++;
        }

        if(d1 == SIZE){
            score = 100;
        }
        else{
            score = d1;
        }
        if(d2 == SIZE){
            score += 100;
        }
        else{
            score += d2;
        }
        return score;
    }
}
