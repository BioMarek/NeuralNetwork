package games.snake;

public enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE;

    public static Direction opposite(Direction direction) {
        Direction result = NONE;
        switch (direction) {
            case UP:
                result = DOWN;
                break;
            case DOWN:
                result = UP;
                break;
            case LEFT:
                result = RIGHT;
                break;
            case RIGHT:
                result = LEFT;
                break;
        }
        return result;
    }
}
