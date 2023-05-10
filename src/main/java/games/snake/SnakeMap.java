package games.snake;

public enum SnakeMap {
    EMPTY(0), WALL(-1), FOOD(1), BODY(100), HEAD(200);

    public final int value;

    SnakeMap(int nominalValue) {
        this.value = nominalValue;
    }
}
