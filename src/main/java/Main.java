import Evolution.EvolutionEngine;
import Snake.SnakeGame;

public class Main {
    public static void main(String[] args) {
        EvolutionEngine evolutionEngine = new EvolutionEngine(1, new int[] {1, 1, 1});
        evolutionEngine.playSnake();
    }
}
