package Bingo;

public class Player {
    int points = 0;

    private void incrementPoint() {
        ++this.points;
    }

    private void decrementPoint() {
        --this.points;
    }
}
