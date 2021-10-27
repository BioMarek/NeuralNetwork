package Evolution;

import NeuralNetwork.NeuralNetwork;
import Snake.SnakeGame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EvolutionEngine {
    // TODO needs redesign
    List<NeuralNetwork> neuralNetworks = new ArrayList<>();
    Function<Double, Double> hiddenLayerActivationFunc;
    Function<Double, Double> outputLayerActivationFunc;

    public EvolutionEngine(int numOfNetworks,
                           int[] neuralNetworkSettings,
                           Function<Double, Double> hiddenLayerActivationFunc,
                           Function<Double, Double> outputLayerActivationFunc) {
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;

        for (int i = 0; i < numOfNetworks; i++) {
            this.neuralNetworks.add(new NeuralNetwork(neuralNetworkSettings, hiddenLayerActivationFunc, outputLayerActivationFunc));
        }
    }

    public void playSnake() {
        NeuralNetwork nn = new NeuralNetwork(new int[]{8, 10, 4}, hiddenLayerActivationFunc, outputLayerActivationFunc);
        SnakeGame snakeGame = new SnakeGame(20);

        for (int i = 0; i < 10; i++) {
            double[] networkOutput = nn.getNetworkOutput(snakeGame.snakeMapper().getInput());
            snakeGame.processNeuralNetworkMove(translateOutputToKey(networkOutput));
            if (snakeGame.isGameOver)
                break;
        }
    }

    public String translateOutputToKey(double[] neuralNetworkOutput) {
        int maxIndex = 0;
        double max = neuralNetworkOutput[0];

        for (int i = 0; i < neuralNetworkOutput.length; i++) {
            if (max < neuralNetworkOutput[i]) {
                max = neuralNetworkOutput[i];
                maxIndex = i;
            }
        }

        switch (maxIndex) {
            case 0:
                return "w";
            case 1:
                return "s";
            case 2:
                return "a";
            case 3:
                return "d";
        }

        return "error";
    }

    public void nextGeneration() {

    }
}
