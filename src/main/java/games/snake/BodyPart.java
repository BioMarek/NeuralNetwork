package games.snake;

public class BodyPart {
    public boolean isHead;
    public int row;
    public int column;

    public BodyPart(boolean isHead, int row, int column) {
        this.isHead = isHead;
        this.row = row;
        this.column = column;
    }
}
