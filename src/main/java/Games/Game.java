package Games;

import Interfaces.NeuralNetwork;

public interface Game {
    void play(NeuralNetwork neuralNetwork, int maxNumberOfMoves);

    void reset();
}
