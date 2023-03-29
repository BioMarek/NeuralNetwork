package basic_neural_network.neural_network;

import interfaces.NeuralNetwork;
import visualizations.dtos.VisLayerDTO;
import visualizations.dtos.VisualizationDTO;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link BasicNeuralNetwork} is composed of {@link Layer}. Hidden layers compute result based on input supplied
 * into first layer. Results of computations are propagated into subsequent layers up to last layer which output is final
 * output of {@link BasicNeuralNetwork}.
 */
@EqualsAndHashCode
public class BasicNeuralNetwork implements Comparable<BasicNeuralNetwork>, Serializable, NeuralNetwork {
    public final List<Layer> hiddenLayers;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public int score = 0;
    public String name = "0";
    public int age = 0;

    /**
     * @param sizes                     Array of sizes, first number is number of {@link BasicNeuralNetwork} inputs.
     *                                  Numbers size[1]...size[n] say how many {@link BasicNeuron} should be in n-th
     *                                  hidden {@link Layer}.
     * @param hiddenLayerActivationFunc activation function used in hidden layers
     * @param outputLayerActivationFunc activation function used in output layer
     */
    public BasicNeuralNetwork(int[] sizes,
                              Function<Double, Double> hiddenLayerActivationFunc,
                              Function<Double, Double> outputLayerActivationFunc) {
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;

        if (sizes.length < 1)
            throw new IllegalArgumentException("Neural network needs at least 1 hidden layer");

        hiddenLayers = new ArrayList<>();
        for (int i = 0; i < sizes.length - 1; i++) {
            hiddenLayers.add(new Layer(sizes[i], sizes[i + 1], i));
        }
    }

    public BasicNeuralNetwork(List<Layer> hiddenLayers, int score, String name) {
        this.hiddenLayers = hiddenLayers;
        this.score = score;
        this.name = name;
    }

    /**
     * @return deep copy of {@link BasicNeuralNetwork}
     */
    public BasicNeuralNetwork copy() {
        BasicNeuralNetwork copy = new BasicNeuralNetwork(
                hiddenLayers.stream()
                        .map(Layer::copy)
                        .collect(Collectors.toList()), score, name
        );
        copy.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        copy.outputLayerActivationFunc = outputLayerActivationFunc;
        return copy;
    }

    /**
     * The function takes input vector and feeds it into {@link BasicNeuralNetwork}. Results of {@link Layer} is fed as
     * input into next and result of last {@link Layer is presented as output of {@link BasicNeuralNetwork } computation.
     *
     * @param inputs to evaluate
     * @return vector of values copmuted by {@link BasicNeuralNetwork }
     */
    @Override
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

    @Override
    public VisualizationDTO getVisualizationDTO() {
        int neuronNames = 0;
        VisualizationDTO visualizationDTO = new VisualizationDTO(hiddenLayers.size());

        for (int i = 0; i < hiddenLayers.size(); i++) {
            VisLayerDTO visLayerDTO = visualizationDTO.layers.get(i);
            for (BasicNeuron basicNeuron : hiddenLayers.get(i).basicNeurons) {
//                visLayerDTO.addNeuron(neuronNames++);
//                visLayerDTO.connections // TODO
            }
        }

        return visualizationDTO.build();
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
    public void printNetwork() {
        for (int i = 0; i < hiddenLayers.size(); i++) {
            System.out.println("Layer: " + i);
            hiddenLayers.get(i).printLayer();
        }
    }

    /**
     * Saves {@link BasicNeuralNetwork} into file. {@link NeuralNetworkDTO} is used as intermediate object which is
     * actually saved into file. {@link NeuralNetworkDTO} is missing lambdas hiddenLayerActivationFunc,
     * outputLayerActivationFunc which cannot be easily saved.
     *
     * @param filePath path to file where the {@link BasicNeuralNetwork} will be saved. Name of the
     *                 {@link BasicNeuralNetwork} is appended to file name.
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
     * The function loads {@link NeuralNetworkDTO} from file and converts it into {@link BasicNeuralNetwork}. Activation
     * functions cannot be easily saved and loaded from file therefore they have to be supplied.
     *
     * @param filePath                  path to file where {@link BasicNeuralNetwork} is saved
     * @param hiddenLayerActivationFunc activation function used in hidden layers
     * @param outputLayerActivationFunc activation function used in output layer
     * @return {@link BasicNeuralNetwork} loaded from file
     */
    public BasicNeuralNetwork loadFromFile(String filePath, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        NeuralNetworkDTO neuralNetworkDTO;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
            neuralNetworkDTO = (NeuralNetworkDTO) objectInputStream.readObject();

            BasicNeuralNetwork neuralNetwork = new BasicNeuralNetwork(neuralNetworkDTO.hiddenLayers, neuralNetworkDTO.score, neuralNetworkDTO.name);
            neuralNetwork.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
            neuralNetwork.outputLayerActivationFunc = outputLayerActivationFunc;

            objectInputStream.close();
            return neuralNetwork;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        throw new RuntimeException("Failed to load BasicNeuralNetwork.NeuralNetwork");
    }

    @Override
    public int compareTo(BasicNeuralNetwork neuralNetwork) {
        if (this.score == neuralNetwork.score)
            return 0;
        return (this.score > neuralNetwork.score) ? 1 : -1;
    }
}
