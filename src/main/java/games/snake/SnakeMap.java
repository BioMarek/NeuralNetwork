package games.snake;

public enum SnakeMap {
    // TODO refactor numbers wall 1, food, 2 or negative numbers
    EMPTY(0), WALL(1), BODY(2), HEAD(3), FOOD(4);
    public final int value;

    SnakeMap(int nominalValue) {
        this.value = nominalValue;
    }
}
