package Bingo;

/**
 * Strategy interface for AI move selection.
 * Allows plug-and-play of Easy (Random), Medium (DP), and Hard (Minimax) strategies.
 */
public interface MoveStrategy {
    /**
     * Choose the best value to call on this turn.
     * @param computer  the computer's board (used to find candidate moves)
     * @param player    the player's board (used to evaluate opponent state)
     * @return the integer value to call, or -1 if no move is available
     */
    int findBestMove(BingoBoard computer, BingoBoard player);
}
