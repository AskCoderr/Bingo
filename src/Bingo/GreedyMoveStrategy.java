package Bingo;
public class GreedyMoveStrategy {
    public int findBestMove(BingoBoard compBoard){
        int size = compBoard.size;
        Result res = D_C(compBoard, 0, 0,size-1,size-1);
        return res.number;
    }
    private static class Result{
        int score;
        int number;
        Result(int score, int number){
            this.score = score;
            this.number = number;
        }
    }
    private Result D_C(BingoBoard board , int r1, int c1, int r2, int c2){
        if(r1==r2&&c1==c2){
            Node node = board.nodes[r1][c1];
            if (node.marked){
                return new Result(-1,-1);
            }
            node.marked = true;
            int score = EvaluateBoard(board);
            node.marked = false;
            return new Result(score, node.value);
        }
        int rm = (r1+r2)/2;
        int cm = (c1+c2)/2;
        Result best = new Result(Integer.MIN_VALUE,-1);
        best = max(best, D_C(board, r1,c1,rm,cm));
        if(cm+1<=c2){
            best = max(best,D_C(board, r1, cm+1, rm,c2));
        }
        if(rm+1<=r2){
             best = max(best,D_C(board, rm + 1, c1, r2, cm));
        }
        if(rm+1<=r2&&cm+1<=c2){
             best = max(best,D_C(board, rm + 1, cm + 1, r2, c2));
        }
        return best;
    }
    private Result max(Result a , Result b ){
        return b.score>a.score?b:a;
    }
    private int EvaluateBoard(BingoBoard b){
        int score = 0;
        int SIZE = b.size;
        for(int i = 0;i<SIZE;i++){
            score+=countRows(b,i);
            score+=countColumns(b,i);
        }
        score+=countDiagonals(b);
        return score;
    }
      private int countRows(BingoBoard b, int index) {
        int SIZE = b.size;
        int count = 0;
        for (int k = 0; k <SIZE; k++) {
            if (b.nodes[index][k].marked) count++;
        }
        return (count == SIZE) ?100 : count;
    }
    private int countColumns(BingoBoard b , int index){
        int SIZE = b.size;
        int count = 0;
        for (int k = 0; k < SIZE; k++) {
            if (b.nodes[k][index].marked) count++;
        }
        return (count == SIZE) ? 100 : count;
    }
    private int countDiagonals(BingoBoard b){
        int SIZE = b.size;
        int d1 = 0;
        int d2 = 0;
        for(int i = 0;i<SIZE;i++){
            if(b.nodes[i][i].marked){
                d1++;
            }
            if (b.nodes[i][SIZE - i - 1].marked) {
                d2++;
            }
        }
        int score = (d1 == SIZE ? 100 : d1);
        score += (d2 == SIZE ? 100 : d2);
        return score;
    }
}

            

        
    
