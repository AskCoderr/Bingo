package Bingo;

import java.util.Random;

/**
 * EASY difficulty: picks a random unmarked cell on the computer's board.
 */
public class RandomMoveStrategy implements MoveStrategy {

    private final Random rand = new Random();

    @Override
    public int findBestMove(BingoBoard computer, BingoBoard player) {
        // Retry up to 200 times to find an unmarked cell randomly
        for (int tries = 0; tries < 200; tries++) {
            int i = rand.nextInt(computer.size);
            int j = rand.nextInt(computer.size);
            if (!computer.nodes[i][j].marked)
                return computer.nodes[i][j].value;
        }
        // Fallback: linear scan for first unmarked
        for (int i = 0; i < computer.size; i++)
            for (int j = 0; j < computer.size; j++)
                if (!computer.nodes[i][j].marked)
                    return computer.nodes[i][j].value;
        return -1;
    }
}
