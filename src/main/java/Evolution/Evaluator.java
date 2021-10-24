package Evolution;

import NeuralNetwork.NeuralNetwork;

public class Evaluator {
    /**
     * Evaluates outputs of {@link NeuralNetwork}
     *
     * @param neuralNetwork that evaluates input produces and produces output
     * @param inputs        array of inputs
     * @return score given by scoring function
     */
    public int evaluate(NeuralNetwork neuralNetwork, double[] inputs) {
        return getScore(neuralNetwork.getNetworkOutput(inputs));
    }

    /**
     * Scoring function gives score to each particular output
     *
     * @param output {@link NeuralNetwork} output
     * @return score
     */
    public int getScore(double[] output) {
        return 1;
    }
}
