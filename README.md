# Bingo Game (AI Enabled)

A Java-based 5x5 Bingo game featuring an intelligent Computer opponent powered by **Dynamic Programming** and **Divide & Conquer** algorithms.

## How to Run

1.  **Compile**:
    ```bash
    javac -d bin src/Bingo/*.java
    ```
2.  **Run**:
    ```bash
    java -cp bin Bingo.Main
    ```

## Key Algorithms

### 1. Divide & Conquer (Win Checking)
**Location**: `BingoBoardAnalyser.java` -> `checkRange()`

Instead of checking rows and columns with simple iterative loops (`for` loops), we use a recursive **Divide & Conquer** approach.

*   **Logic**: To check if a line of $N$ cells is fully marked:
    1.  **Divide**: Split the line into two halves (Left and Right).
    2.  **Conquer**: Recursively check if the Left half is marked AND if the Right half is marked.
    3.  **Base Case**: If the range is size 1, return true if that single node is marked.
*   **Recurrence Relation**: $T(n) = 2T(n/2) + O(1)$, which solves to $O(n)$ time complexity (same as iteration, but demonstrates the algorithmic principle).

```java
private boolean checkRange(Node[] line, int start, int end) {
    if (start == end) return line[start].marked;
    int mid = (start + end) / 2;
    return checkRange(line, start, mid) && checkRange(line, mid + 1, end);
}
```

### 2. Dynamic Programming (AI Strategy)
**Location**: `DPMoveStrategy.java` -> `findBestMove()`

The Computer uses **Top-Down Dynamic Programming with Memoization** to select the best move. It acts as an aggressive, maximizing agent.

*   **State Key**: A unique string representing the board's configuration (which cells are marked/unmarked).
*   **Memoization**: We use a `HashMap` (`memo`) to store the calculated score of every board state we encounter. If we see the same board state again, we return the stored score instantly ($O(1)$) instead of recalculating.
*   **Evaluation Function**:
    *   The score for a state is calculated by summing the "power" of all potential lines.
    *   **Winning Line**: Worth **1,000,000 points**.
    *   **Near-Complete Line**: Worth exponentially less ($10^k$).
*   **Aggressive Heuristic**:
    The AI calculates the "Net Score" to decide a move:
    $$NetScore = Score(Computer) - (0.5 \times Score(Player))$$
    *   It prioritizes increasing its own score.
    *   It subtracts the Player's score to avoid giving away easy wins, but keeping the weight low ($0.5$) ensures it stays aggressive rather than purely defensive.

## Project Structure
*   `BingoBoard.java`: Data structure for the grid.
*   `BingoBoardAnalyser.java`: Game engine using D&C for rules.
*   `DPMoveStrategy.java`: The Brains (DP + Memoization).
*   `BingoGUI.java`: Swing interface.
