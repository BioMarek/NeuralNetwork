package Interfaces;

import NeuralNetwork.NeuralNetwork;

public interface Game {
    void play(NeuralNetwork neuralNetwork, int maxNumberOfMoves);

    void reset();
}
