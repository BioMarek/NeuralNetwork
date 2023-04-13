package games;

import games.snake.dtos.SavedGameDTO;
import interfaces.NeuralNetwork;

import java.util.List;

public interface MultiplayerGame {
    int[] play(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves);

    void reset();

    SavedGameDTO saveSnakeMoves(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves);
}
