package games;

import interfaces.NeuralNetwork;

public interface Game {
    int play(NeuralNetwork neuralNetwork, int maxNumberOfMoves);

    void reset();
}
