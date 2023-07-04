package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class FinalScreenLetters {
    private final int[][] grid;

    public FinalScreenLetters(int[][] grid) {
        this.grid = grid;
    }

    public void finalScreen() {
        insertIntoGrid(letterS(), SnakeMap.FOOD.value);
        insertIntoGrid(letterN(), SnakeMap.FOOD.value);
    }

    public void insertIntoGrid(List<Pair<Integer>> coordinates, int number) {
        for (Pair<Integer> pair : coordinates) {
            grid[pair.getFirst()][pair.getSecond()] = number;
        }
    }

    public List<Pair<Integer>> letterS() {
        List<Pair<Integer>> coordinates = new ArrayList<>();
        coordinates.add(new Pair<>(7, 7));
        coordinates.add(new Pair<>(7, 8));
        coordinates.add(new Pair<>(7, 9));
        coordinates.add(new Pair<>(7, 10));

        coordinates.add(new Pair<>(8, 7));
        coordinates.add(new Pair<>(9, 7));
        coordinates.add(new Pair<>(10, 7));
        coordinates.add(new Pair<>(11, 7));
        coordinates.add(new Pair<>(12, 7));

        coordinates.add(new Pair<>(12, 8));
        coordinates.add(new Pair<>(12, 9));
        coordinates.add(new Pair<>(12, 10));

        coordinates.add(new Pair<>(13, 10));
        coordinates.add(new Pair<>(14, 10));
        coordinates.add(new Pair<>(15, 10));
        coordinates.add(new Pair<>(16, 10));
        coordinates.add(new Pair<>(17, 10));

        coordinates.add(new Pair<>(17, 9));
        coordinates.add(new Pair<>(17, 8));
        coordinates.add(new Pair<>(17, 7));
        return coordinates;
    }

    public List<Pair<Integer>> letterN() {
        List<Pair<Integer>> coordinates = new ArrayList<>();
        coordinates.add(new Pair<>(7, 13));
        coordinates.add(new Pair<>(8, 13));
        coordinates.add(new Pair<>(9, 13));
        coordinates.add(new Pair<>(10, 13));
        coordinates.add(new Pair<>(11, 13));
        coordinates.add(new Pair<>(12, 13));
        coordinates.add(new Pair<>(13, 13));
        coordinates.add(new Pair<>(14, 13));
        coordinates.add(new Pair<>(15, 13));
        coordinates.add(new Pair<>(16, 13));
        coordinates.add(new Pair<>(17, 13));

        coordinates.add(new Pair<>(8, 14));
        coordinates.add(new Pair<>(9, 14));
        coordinates.add(new Pair<>(10, 15));
        coordinates.add(new Pair<>(11, 15));
        coordinates.add(new Pair<>(12, 16));
        coordinates.add(new Pair<>(13, 17));
        coordinates.add(new Pair<>(14, 17));
        coordinates.add(new Pair<>(15, 18));
        coordinates.add(new Pair<>(16, 18));

        coordinates.add(new Pair<>(7, 19));
        coordinates.add(new Pair<>(8, 19));
        coordinates.add(new Pair<>(9, 19));
        coordinates.add(new Pair<>(10, 19));
        coordinates.add(new Pair<>(11, 19));
        coordinates.add(new Pair<>(12, 19));
        coordinates.add(new Pair<>(13, 19));
        coordinates.add(new Pair<>(14, 19));
        coordinates.add(new Pair<>(15, 19));
        coordinates.add(new Pair<>(16, 19));
        coordinates.add(new Pair<>(17, 19));
        return coordinates;
    }

}
