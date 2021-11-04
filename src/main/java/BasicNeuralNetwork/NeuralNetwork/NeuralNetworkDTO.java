package BasicNeuralNetwork.NeuralNetwork;

import java.io.Serializable;
import java.util.List;

public class NeuralNetworkDTO implements Serializable {
    public List<Layer> hiddenLayers;
    public int score;
    public String name;

    public NeuralNetworkDTO(List<Layer> hiddenLayers, int score, String name) {
        this.hiddenLayers = hiddenLayers;
        this.score = score;
        this.name = name;
    }
}
