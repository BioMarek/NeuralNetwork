package games.snake;

public enum SnakeMap {
    EMPTY(0), WALL(1), FOOD(2), BODY(3), HEAD(4);
    public final int value;

    SnakeMap(int nominalValue) {
        this.value = nominalValue;
    }
}
