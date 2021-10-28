package NeuralNetwork;

import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link NeuralNetwork} is composed of {@link Layer}. Hidden layers compute result based on input supplied into
 * first layer. Results of computations are propagated into subsequent layers up to last layer which output is final
 * output of {@link NeuralNetwork}.
 */
@EqualsAndHashCode
public class NeuralNetwork implements Comparable<NeuralNetwork>, Serializable {
    public final List<Layer> hiddenLayers;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public int score = 0;
    public String name = "0";

    /**
     * @param sizes                     Array of sizes, first number is number of {@link NeuralNetwork} inputs. Numbers
     *                                  size[1]...size[n] say how many {@link Neuron} should be in n-th hidden {@link Layer}.
     * @param hiddenLayerActivationFunc activation function used in hidden layers
     * @param outputLayerActivationFunc activation function used in output layer
     */
    public NeuralNetwork(int[] sizes, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;

        if (sizes.length < 1)
            throw new IllegalArgumentException("Neural network needs at least 1 hidden layer");

        hiddenLayers = new ArrayList<>();
        for (int i = 0; i < sizes.length - 1; i++) {
            hiddenLayers.add(new Layer(sizes[i], sizes[i + 1], i));
        }
    }

    public NeuralNetwork(List<Layer> hiddenLayers, int score, String name) {
        this.hiddenLayers = hiddenLayers;
        this.score = score;
        this.name = name;
    }

    /**
     * @return deep copy of {@link NeuralNetwork}
     */
    public NeuralNetwork copy() {
        NeuralNetwork copy = new NeuralNetwork(
                hiddenLayers.stream()
                        .map(x -> x.copy())
                        .collect(Collectors.toList()), score, name
        );
        copy.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        copy.outputLayerActivationFunc = outputLayerActivationFunc;
        return copy;
    }

    /**
     * The function takes input vector and feeds it into {@link NeuralNetwork}. Results of {@link Layer} is fed as input
     * into next and result of last {@link Layer is presented as output of {@link NeuralNetwork} computation.
     *
     * @param inputs to evaluate
     * @return vector of values copmuted by {@link NeuralNetwork}
     */
    public double[] getNetworkOutput(double[] inputs) {
        int outputLayerIndex = hiddenLayers.size() - 1;
        hiddenLayers.get(0).calculateOutput(inputs, hiddenLayerActivationFunc);

        for (int i = 1; i < hiddenLayers.size() - 1; i++) {
            hiddenLayers.get(i).calculateOutput(
                    hiddenLayers.get(i - 1).layerOutputs,
                    hiddenLayerActivationFunc);
        }
        hiddenLayers.get(outputLayerIndex).calculateOutput(
                hiddenLayers.get(outputLayerIndex - 1).layerOutputs,
                outputLayerActivationFunc);

        return hiddenLayers.get(outputLayerIndex).layerOutputs;
    }

    /**
     * The function mutates all hidden layers.
     *
     * @param numOfNeuronsToMutate number of neurons that should be mutated in each layer
     * @param numOfMutations       number of weight changes in each neuron
     */
    public void mutateLayers(int numOfNeuronsToMutate, int numOfMutations) {
        hiddenLayers.forEach((layer -> layer.mutateRandomNeuron(numOfNeuronsToMutate, numOfMutations)));
    }

    /**
     * Prints neural network in human-readable format.
     */
    public void printNeuralNetwork() {
        for (int i = 0; i < hiddenLayers.size(); i++) {
            System.out.println("Layer: " + i);
            hiddenLayers.get(i).printLayer();
        }
    }

    /**
     * Saves {@link NeuralNetwork} into file. {@link NeuralNetworkDTO} is used as intermediate object which is actually
     * saved into file. {@link NeuralNetworkDTO} is missing lambdas hiddenLayerActivationFunc, outputLayerActivationFunc
     * which cannot be easily saved.
     *
     * @param filePath path to file where the {@link NeuralNetwork} will be saved. Name of the {@link NeuralNetwork} is
     *                 appended to file name.
     * @param info     string appended to file. It is used describe {@link NeuralNetworkDTO} saved in file.
     */
    public void saveToFile(String filePath, String info) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath + "_" + name + "_" + info));
            objectOutputStream.writeObject(new NeuralNetworkDTO(hiddenLayers, score, name));
            objectOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The function loads {@link NeuralNetworkDTO} from file and converts it into {@link NeuralNetwork}. Activation
     * functions cannot be easily saved and loaded from file therefore they have to be supplied.
     *
     * @param filePath                  path to file where {@link NeuralNetwork} is saved
     * @param hiddenLayerActivationFunc activation function used in hidden layers
     * @param outputLayerActivationFunc activation function used in output layer
     * @return {@link NeuralNetwork} loaded from file
     */
    public NeuralNetwork loadFromFile(String filePath, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        NeuralNetworkDTO neuralNetworkDTO;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
            neuralNetworkDTO = (NeuralNetworkDTO) objectInputStream.readObject();

            NeuralNetwork neuralNetwork = new NeuralNetwork(neuralNetworkDTO.hiddenLayers, neuralNetworkDTO.score, neuralNetworkDTO.name);
            neuralNetwork.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            neuralNetwork.outputLayerActivationFunc = outputLayerActivationFunc;

            objectInputStream.close();
            return neuralNetwork;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to load NeuralNetwork");
    }

    @Override
    public int compareTo(NeuralNetwork neuralNetwork) {
        if (this.score == neuralNetwork.score)
            return 0;
        return (this.score > neuralNetwork.score) ? 1 : -1;
    }
}
