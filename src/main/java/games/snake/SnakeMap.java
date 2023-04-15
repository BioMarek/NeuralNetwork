package games.snake;

public enum SnakeMap {
    EMPTY(0), WALL(1), FOOD(2), BODY(3), HEAD(4),
    BODY_MULTIPLAYER(100), HEAD_MULTIPLAYER(200);
    public final int value;

    SnakeMap(int nominalValue) {
        this.value = nominalValue;
    }
}
