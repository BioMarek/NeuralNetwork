package NEAT.Phenotype;

import Interfaces.NeuralNetwork;
import NEAT.Evolution.GenePool;
import NEAT.NeuronType;
import Utils.Util;
import Visualizations.DTOs.VisConnectionDTO;
import Visualizations.DTOs.VisLayerDTO;
import Visualizations.DTOs.VisNeuronDTO;
import Visualizations.DTOs.VisualizationDTO;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Utils.Util.repeat;

/**
 * The class represents properties of NEAT network derived particular genotype. Genotypes are stored in {@link GenePool}
 */
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Phenotype implements NeuralNetwork {
    public GenePool genePool;
    public List<NEATNeuron> inputNeurons;
    public List<NEATNeuron> hiddenNeurons;
    public List<NEATNeuron> outputNeurons;
    public List<Connection> connections;

    public Phenotype(GenePool genePool, List<NEATNeuron> neurons, List<Connection> connections) {
        this.genePool = genePool;
        this.inputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.INPUT).collect(Collectors.toList());
        this.hiddenNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.HIDDEN).collect(Collectors.toList());
        this.outputNeurons = neurons.stream().filter(neatNeuron -> neatNeuron.neuronType == NeuronType.OUTPUT).collect(Collectors.toList());
        this.connections = connections;
    }

    /**
     * Resets inner potentials of all neurons.
     */
    public void resetPhenotype() {
        inputNeurons.forEach(NEATNeuron::reset);
        hiddenNeurons.forEach(NEATNeuron::reset);
        outputNeurons.forEach(NEATNeuron::reset);
    }

    /**
     * Calculates output of {@link Phenotype} using supplied activation function. Activation function used in input
     * neurons is {@link Util#activationFunctionIdentity()}.
     *
     * @param inputs double array of inputs
     * @return double array of outputs
     */
    @Override
    public double[] getNetworkOutput(double[] inputs) {
        resetPhenotype();

        for (int i = 0; i < inputs.length; i++) {
            inputNeurons.get(i).innerPotential = inputs[i];
        }
        connections.forEach(connection -> connection.to.innerPotential += connection.from.getOutput(genePool.hiddenLayerActivationFunc) * connection.weight);

        return Util.primitiveDoubleArrayFromList(
                outputNeurons.stream()
                        .map(neatNeuron -> neatNeuron.getOutput(genePool.outputLayerActivationFunc))
                        .collect(Collectors.toList()));
    }

    @Override
    public VisualizationDTO getVisualizationDTO() {
        int numOfLayers = 2 + hiddenNeurons.stream()
                .mapToInt(neatNeuron -> neatNeuron.layer)
                .max()
                .orElse(0);

        VisualizationDTO visualizationDTO = new VisualizationDTO();
        List<VisLayerDTO> layerDTOS = new ArrayList<>();
        repeat.accept(numOfLayers, () -> layerDTOS.add(new VisLayerDTO()));

        // TODO try better approach
        for (Connection connection : connections) {
            int toLayer = (connection.to.layer == 1000) ? numOfLayers - 1 : connection.to.layer;
            VisNeuronDTO from = new VisNeuronDTO(connection.from.name, connection.from.layer);
            VisNeuronDTO to = new VisNeuronDTO(connection.to.name, toLayer);

            layerDTOS.get(connection.from.layer).connections.add(new VisConnectionDTO(from, to, connection.weight));
            layerDTOS.get(connection.from.layer).addNeuron(from);
            layerDTOS.get(toLayer).addNeuron(to);
        }
        visualizationDTO.layers = layerDTOS;

        return visualizationDTO.build();
    }

    /**
     * Prints {@link Phenotype} neural network in human-readable format.
     */
    @Override
    public void printNetwork() {
        connections.forEach(connectionGene -> System.out.printf("%-3d -> %-4d %7.4f%n", connectionGene.from.name, connectionGene.to.name, connectionGene.weight));
    }
}
