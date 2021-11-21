package Games;

import Interfaces.NeuralNetwork;

public interface Game {
    int play(NeuralNetwork neuralNetwork, int maxNumberOfMoves);

    void reset();
}
