package Evolution;

import NeuralNetwork.NeuralNetwork;

public class Evaluator {
    public int evaluate(NeuralNetwork neuralNetwork){
        int score = 0;
        score += getScore(neuralNetwork.getNetworkOutput(getInputs()));
        return score;
    }

    public int getScore(double[] output){
        return 1;
    }

    public double[] getInputs(){
        return new double[]{0};
    }
}
