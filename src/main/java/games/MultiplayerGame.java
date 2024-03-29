package games;

import games.snake.savegame.SavedGameDTO;
import neat.phenotype.NeuralNetwork;

import java.util.List;

public interface MultiplayerGame {
    int[] play(List<NeuralNetwork> neuralNetworks);

    void reset();

    SavedGameDTO saveSnakeMoves(List<NeuralNetwork> neuralNetworks);
}
