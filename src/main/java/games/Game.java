package games;

import games.snake.savegame.SavedGameDTO;
import interfaces.NeuralNetwork;

public interface Game {
    int play(NeuralNetwork neuralNetwork, int maxNumberOfMoves);

    void reset();

    SavedGameDTO saveSnakeMoves(NeuralNetwork neuralNetwork, int maxNumberOfMoves);
}
