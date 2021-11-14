package NEAT.Phenotype;

import NEAT.NeuronType;
import Utils.Util;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode
public class Phenotype {
    public List<NEATNeuron> inputNeurons;
    public List<NEATNeuron> hiddenNeurons;
    public List<NEATNeuron> outputNeurons;
    public List<NEATNeuron> neurons;
    public List<Connection> connections;

    public Phenotype(List<NEATNeuron> neurons) {
        this.neurons = neurons;
        this.inputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.INPUT).collect(Collectors.toList());
        this.hiddenNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.HIDDEN).collect(Collectors.toList());
        this.outputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.OUTPUT).collect(Collectors.toList());
        connections = new ArrayList<>();
    }

    public double[] evaluate(double[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            inputNeurons.get(i).bias = inputs[i];
        }
        for (Connection connection : connections) {
            if (connection.from.neuronType == NeuronType.INPUT) {
                connection.to.innerPotential += connection.from.getOutput(Util.activationFunctionIdentity());
            } else connection.to.innerPotential += connection.from.getOutput(Util.activationFunctionUnitStep());
        }

        double[] result = new double[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            result[i] = outputNeurons.get(i).getOutput(Util.activationFunctionUnitStep());
        }
        return result;
    }
}
