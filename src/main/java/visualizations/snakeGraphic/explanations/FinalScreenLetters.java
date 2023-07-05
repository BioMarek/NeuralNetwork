package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class FinalScreenLetters {

    public int finalScreenInsert(int[][] grid) {
        var foodPlaced = 0;
        foodPlaced += insertIntoGrid(grid, letterS(), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterN(), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterA(), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterK(), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterE(), SnakeMap.FOOD.value);
        return foodPlaced;
    }

    public int insertIntoGrid(int[][] grid, List<Pair<Integer>> coordinates, int number) {
        for (Pair<Integer> pair : coordinates) {
            grid[pair.getFirst()][pair.getSecond()] = number;
        }
        return coordinates.size();
    }

    public List<Pair<Integer>> letterS() {
        List<Pair<Integer>> coordinates = new ArrayList<>();
        coordinates.add(new Pair<>(7, 5));
        coordinates.add(new Pair<>(7, 6));
        coordinates.add(new Pair<>(7, 7));
        coordinates.add(new Pair<>(7, 8));

        coordinates.add(new Pair<>(8, 5));
        coordinates.add(new Pair<>(9, 5));
        coordinates.add(new Pair<>(10, 5));
        coordinates.add(new Pair<>(11, 5));
        coordinates.add(new Pair<>(12, 5));

        coordinates.add(new Pair<>(12, 6));
        coordinates.add(new Pair<>(12, 7));
        coordinates.add(new Pair<>(12, 8));

        coordinates.add(new Pair<>(13, 8));
        coordinates.add(new Pair<>(14, 8));
        coordinates.add(new Pair<>(15, 8));
        coordinates.add(new Pair<>(16, 8));
        coordinates.add(new Pair<>(17, 8));

        coordinates.add(new Pair<>(17, 7));
        coordinates.add(new Pair<>(17, 6));
        coordinates.add(new Pair<>(17, 5));
        return coordinates;
    }

    public List<Pair<Integer>> letterN() {
        List<Pair<Integer>> coordinates = new ArrayList<>();
        coordinates.add(new Pair<>(7, 11));
        coordinates.add(new Pair<>(8, 11));
        coordinates.add(new Pair<>(9, 11));
        coordinates.add(new Pair<>(10, 11));
        coordinates.add(new Pair<>(11, 11));
        coordinates.add(new Pair<>(12, 11));
        coordinates.add(new Pair<>(13, 11));
        coordinates.add(new Pair<>(14, 11));
        coordinates.add(new Pair<>(15, 11));
        coordinates.add(new Pair<>(16, 11));
        coordinates.add(new Pair<>(17, 11));

        coordinates.add(new Pair<>(8, 12));
        coordinates.add(new Pair<>(9, 12));
        coordinates.add(new Pair<>(10, 13));
        coordinates.add(new Pair<>(11, 13));
        coordinates.add(new Pair<>(12, 14));
        coordinates.add(new Pair<>(13, 15));
        coordinates.add(new Pair<>(14, 15));
        coordinates.add(new Pair<>(15, 16));
        coordinates.add(new Pair<>(16, 16));

        coordinates.add(new Pair<>(7, 17));
        coordinates.add(new Pair<>(8, 17));
        coordinates.add(new Pair<>(9, 17));
        coordinates.add(new Pair<>(10, 17));
        coordinates.add(new Pair<>(11, 17));
        coordinates.add(new Pair<>(12, 17));
        coordinates.add(new Pair<>(13, 17));
        coordinates.add(new Pair<>(14, 17));
        coordinates.add(new Pair<>(15, 17));
        coordinates.add(new Pair<>(16, 17));
        coordinates.add(new Pair<>(17, 17));
        return coordinates;
    }

    public List<Pair<Integer>> letterA() {
        List<Pair<Integer>> coordinates = new ArrayList<>();

        coordinates.add(new Pair<>(7, 24));
        coordinates.add(new Pair<>(8, 23));
        coordinates.add(new Pair<>(9, 23));
        coordinates.add(new Pair<>(10, 22));
        coordinates.add(new Pair<>(11, 22));

        coordinates.add(new Pair<>(8, 25));
        coordinates.add(new Pair<>(9, 25));
        coordinates.add(new Pair<>(10, 26));
        coordinates.add(new Pair<>(11, 26));

        coordinates.add(new Pair<>(12, 22));
        coordinates.add(new Pair<>(12, 23));
        coordinates.add(new Pair<>(12, 24));
        coordinates.add(new Pair<>(12, 25));
        coordinates.add(new Pair<>(12, 26));

        coordinates.add(new Pair<>(13, 21));
        coordinates.add(new Pair<>(14, 21));
        coordinates.add(new Pair<>(15, 21));
        coordinates.add(new Pair<>(16, 20));
        coordinates.add(new Pair<>(17, 20));

        coordinates.add(new Pair<>(13, 27));
        coordinates.add(new Pair<>(14, 27));
        coordinates.add(new Pair<>(15, 27));
        coordinates.add(new Pair<>(16, 28));
        coordinates.add(new Pair<>(17, 28));

        return coordinates;
    }

    public List<Pair<Integer>> letterK() {
        List<Pair<Integer>> coordinates = new ArrayList<>();

        coordinates.add(new Pair<>(7, 31));
        coordinates.add(new Pair<>(8, 31));
        coordinates.add(new Pair<>(9, 31));
        coordinates.add(new Pair<>(10, 31));
        coordinates.add(new Pair<>(11, 31));
        coordinates.add(new Pair<>(12, 31));
        coordinates.add(new Pair<>(13, 31));
        coordinates.add(new Pair<>(14, 31));
        coordinates.add(new Pair<>(15, 31));
        coordinates.add(new Pair<>(16, 31));
        coordinates.add(new Pair<>(17, 31));

        coordinates.add(new Pair<>(7, 35));
        coordinates.add(new Pair<>(8, 34));
        coordinates.add(new Pair<>(9, 34));
        coordinates.add(new Pair<>(10, 33));
        coordinates.add(new Pair<>(11, 33));
        coordinates.add(new Pair<>(12, 32));

        coordinates.add(new Pair<>(13, 33));
        coordinates.add(new Pair<>(14, 33));
        coordinates.add(new Pair<>(15, 34));
        coordinates.add(new Pair<>(16, 34));
        coordinates.add(new Pair<>(17, 35));

        return coordinates;
    }

    public List<Pair<Integer>> letterE() {
        List<Pair<Integer>> coordinates = new ArrayList<>();

        coordinates.add(new Pair<>(7, 38));
        coordinates.add(new Pair<>(8, 38));
        coordinates.add(new Pair<>(9, 38));
        coordinates.add(new Pair<>(10, 38));
        coordinates.add(new Pair<>(11, 38));
        coordinates.add(new Pair<>(12, 38));
        coordinates.add(new Pair<>(13, 38));
        coordinates.add(new Pair<>(14, 38));
        coordinates.add(new Pair<>(15, 38));
        coordinates.add(new Pair<>(16, 38));
        coordinates.add(new Pair<>(17, 38));

        coordinates.add(new Pair<>(7, 39));
        coordinates.add(new Pair<>(7, 40));
        coordinates.add(new Pair<>(7, 41));
        coordinates.add(new Pair<>(7, 42));

        coordinates.add(new Pair<>(12, 39));
        coordinates.add(new Pair<>(12, 40));
        coordinates.add(new Pair<>(12, 41));
        coordinates.add(new Pair<>(12, 42));

        coordinates.add(new Pair<>(17, 39));
        coordinates.add(new Pair<>(17, 40));
        coordinates.add(new Pair<>(17, 41));
        coordinates.add(new Pair<>(17, 42));

        return coordinates;
    }
}
