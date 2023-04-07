package utils;

public enum Direction {
    UP, DOWN, LEFT, RIGHT, NONE;

    public static Direction opposite(Direction direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> NONE;
        };
    }

    public static Direction randomDirection() {
        var randomInt = Util.randomInt(0, 4);
        return switch (randomInt) {
            case 0 -> UP;
            case 1 -> DOWN;
            case 2 -> RIGHT;
            case 3 -> LEFT;
            default -> NONE;
        };
    }
}
