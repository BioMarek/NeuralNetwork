package Evolution;

import NeuralNetwork.NeuralNetwork;
import Snake.SnakeGame;

import java.util.ArrayList;
import java.util.List;

public class EvolutionEngine {
    // TODO needs redesign
    List<NeuralNetwork> neuralNetworks = new ArrayList<NeuralNetwork>();

    public EvolutionEngine(int numOfNetworks, int[] neuralNetworkSettings) {
        for (int i = 0; i < numOfNetworks; i++) {
            this.neuralNetworks.add(new NeuralNetwork(neuralNetworkSettings));
        }
    }

    public void playSnake() {
        NeuralNetwork nn = new NeuralNetwork(new int[]{8, 10, 4});
        SnakeGame snakeGame = new SnakeGame(20);

        for (int i = 0; i < 10; i++) {
            double[] networkOutput = nn.getNetworkOutputRaw(snakeGame.snakeMapper().getInput());
            boolean gameOver = snakeGame.processNeuralNetworkMove(translateOutputToKey(networkOutput));
            System.out.println(gameOver);
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
