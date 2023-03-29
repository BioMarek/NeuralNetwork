package games.snake;

public enum SnakeMap {
    EMPTY(0), WALL(1), BODY(2), HEAD(3), FOOD(4);
    public final int value;

    SnakeMap(int nominalValue) {
        this.value = nominalValue;
    }
}
