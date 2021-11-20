package Games;

import Interfaces.INeuralNetwork;

public interface Game {
    void play(INeuralNetwork neuralNetwork, int maxNumberOfMoves);

    void reset();
}
