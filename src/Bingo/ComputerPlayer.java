package Bingo;

public class ComputerPlayer {
    int points = 0;

    private void incrementPoint() {
        ++this.points;
    }

    private void decrementPoint() {
        --this.points;
    }
    public class Player {
        int completedLines = 0;

        public void incrementLine() {
            completedLines++;
        }

        public boolean hasBingo() {
            return completedLines >= 5;
        }
    }

}
