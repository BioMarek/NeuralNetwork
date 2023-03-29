package games;

import interfaces.NeuralNetwork;

import java.util.List;

public interface MultiplayerGame {
    int play(List<NeuralNetwork> neuralNetworks, int maxNumberOfMoves);

    void reset();
}
