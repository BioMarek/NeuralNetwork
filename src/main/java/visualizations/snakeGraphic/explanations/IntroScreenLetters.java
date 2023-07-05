package visualizations.snakeGraphic.explanations;

import games.snake.SnakeMap;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class IntroScreenLetters {

    public int signInsert(int[][] grid) {
        var foodPlaced = 0;
        foodPlaced += insertIntoGrid(grid, letterS(1, 0), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterN(1, 1), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterA(1, 1), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterK(1, 1), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterE(1, 1), SnakeMap.FOOD.value);
        foodPlaced += insertIntoGrid(grid, letterS(1, 37), SnakeMap.FOOD.value);
        return foodPlaced;
    }

    public int insertIntoGrid(int[][] grid, List<Pair<Integer>> coordinates, int number) {
        for (Pair<Integer> pair : coordinates) {
            grid[pair.getFirst()][pair.getSecond()] = number;
        }
        return coordinates.size();
    }

    public List<Pair<Integer>> letterS(int rowShift, int columnShift) {
        List<Pair<Integer>> coordinates = new ArrayList<>();
        coordinates.add(new Pair<>(rowShift + 7, 3 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 4 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 5 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 6 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 7 + columnShift));

        coordinates.add(new Pair<>(rowShift + 8, 3 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 3 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 3 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 3 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 3 + columnShift));

        coordinates.add(new Pair<>(rowShift + 12, 4 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 5 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 6 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 7 + columnShift));

        coordinates.add(new Pair<>(rowShift + 13, 7 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 7 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 7 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 7 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 7 + columnShift));

        coordinates.add(new Pair<>(rowShift + 17, 6 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 5 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 4 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 3 + columnShift));
        return coordinates;
    }

    public List<Pair<Integer>> letterN(int rowShift, int columnShift) {
        List<Pair<Integer>> coordinates = new ArrayList<>();
        coordinates.add(new Pair<>(rowShift + 7, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 8, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 13, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 8 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 8 + columnShift));

        coordinates.add(new Pair<>(rowShift + 8, 9 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 9 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 10 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 10 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 11 + columnShift));
        coordinates.add(new Pair<>(rowShift + 13, 12 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 12 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 13 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 13 + columnShift));

        coordinates.add(new Pair<>(rowShift + 7, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 8, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 13, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 14 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 14 + columnShift));
        return coordinates;
    }

    public List<Pair<Integer>> letterA(int rowShift, int columnShift) {
        List<Pair<Integer>> coordinates = new ArrayList<>();

        coordinates.add(new Pair<>(rowShift + 7, 20 + columnShift));
        coordinates.add(new Pair<>(rowShift + 8, 19 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 19 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 18 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 18 + columnShift));

        coordinates.add(new Pair<>(rowShift + 8, 21 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 21 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 22 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 22 + columnShift));

        coordinates.add(new Pair<>(rowShift + 12, 18 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 19 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 20 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 21 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 22 + columnShift));

        coordinates.add(new Pair<>(rowShift + 13, 17 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 17 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 17 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 16 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 16 + columnShift));

        coordinates.add(new Pair<>(rowShift + 13, 23 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 23 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 23 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 24 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 24 + columnShift));

        return coordinates;
    }

    public List<Pair<Integer>> letterK(int rowShift, int columnShift) {
        List<Pair<Integer>> coordinates = new ArrayList<>();

        coordinates.add(new Pair<>(rowShift + 7, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 8, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 13, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 26 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 26 + columnShift));

        coordinates.add(new Pair<>(rowShift + 7, 30 + columnShift));
        coordinates.add(new Pair<>(rowShift + 8, 29 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 29 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 28 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 28 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 27 + columnShift));

        coordinates.add(new Pair<>(rowShift + 13, 28 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 28 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 29 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 29 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 30 + columnShift));

        return coordinates;
    }

    public List<Pair<Integer>> letterE(int rowShift, int columnShift) {
        List<Pair<Integer>> coordinates = new ArrayList<>();

        coordinates.add(new Pair<>(rowShift + 7, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 8, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 9, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 10, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 11, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 13, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 14, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 15, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 16, 32 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 32 + columnShift));

        coordinates.add(new Pair<>(rowShift + 7, 33 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 34 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 35 + columnShift));
        coordinates.add(new Pair<>(rowShift + 7, 36 + columnShift));

        coordinates.add(new Pair<>(rowShift + 12, 33 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 34 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 35 + columnShift));
        coordinates.add(new Pair<>(rowShift + 12, 36 + columnShift));

        coordinates.add(new Pair<>(rowShift + 17, 33 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 34 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 35 + columnShift));
        coordinates.add(new Pair<>(rowShift + 17, 36 + columnShift));

        return coordinates;
    }
}
