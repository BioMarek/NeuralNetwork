package games.snake;

public enum SnakeMap {
    // TODO refactor numbers wall 1, food, 2 or negative numbers
    EMPTY(0), WALL(1), FOOD(2), BODY(3), HEAD(4);
    public final int value;

    SnakeMap(int nominalValue) {
        this.value = nominalValue;
    }
}
