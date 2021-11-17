package NEAT.Evolution;

import NEAT.NeuronType;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;
import Utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The class holds all genes of population.
 */
public class GenePool {
    private int maxNeurons = 1000;
    private int numOfGenotypes = 100;
    private int neuronNames = 0;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public List<ConnectionGene> connectionGenes = new ArrayList<>();
    public List<NodeGene> nodeGenes = new ArrayList<>();
    public List<Phenotype> phenotypes = new ArrayList<>();

    public void initGenePool(int inputs, int outputs, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;
        boolean[] allEnabled = Util.booleanArray(numOfGenotypes, true);

        List<NodeGene> inputNodes = new ArrayList<>();
        List<NodeGene> outputNodes = new ArrayList<>();
        for (int i = 0; i < inputs; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.INPUT, allEnabled, neuronNames++);
            inputNodes.add(nodeGene);
            nodeGenes.add(nodeGene);
        }
        for (int i = 0; i < outputs; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.OUTPUT, allEnabled, maxNeurons--);
            outputNodes.add(nodeGene);
            nodeGenes.add(nodeGene);
        }
        for (NodeGene input : inputNodes) {
            for (NodeGene output : outputNodes) {
                connectionGenes.add(new ConnectionGene(input, output, Util.randomDoubleArray(numOfGenotypes), Util.booleanArray(numOfGenotypes, true)));
            }
        }
        Collections.sort(nodeGenes);
        Collections.sort(connectionGenes);
    }

    /**
     * Builds phenotypes from {@link GenePool}
     */
    public void createPhenotypes() {
        phenotypes = new ArrayList<>();
        for (int i = 0; i < numOfGenotypes; i++) {
            int index = i;  // needed for effectively final variable
            LinkedHashMap<Integer, NEATNeuron> neurons = new LinkedHashMap<>();

            nodeGenes.stream()
                    .filter((nodeGene) -> (nodeGene.enabled[index]))
                    .forEach((nodeGene) -> neurons.put(nodeGene.name, new NEATNeuron(nodeGene.name, nodeGene.type)));

            Phenotype phenotype = new Phenotype(new ArrayList<>(neurons.values()));

            connectionGenes.stream()
                    .filter((connectionGene) -> connectionGene.enabled[index])
                    .forEach((connectionGene) -> phenotype.connections.add(new Connection(
                            neurons.get(connectionGene.from.name),
                            neurons.get(connectionGene.to.name),
                            connectionGene.weight[index])));

            phenotypes.add(phenotype);
        }
    }

    /**
     * Assigns new random weight to random connection of given phenotype.
     *
     * @param phenotype phenotype to mutate
     */
    public void randomWeightMutation(int phenotype) {
        connectionGenes
                .get(Util.randomInt(0, connectionGenes.size()))
                .weight[phenotype] = Util.randomDouble();
    }

    /**
     * Splits random {@link ConnectionGene} by introducing new {@link NodeGene} and two new {@link ConnectionGene}s.
     * First new {@link ConnectionGene} goes from old {@link ConnectionGene} "from" {@link NodeGene} to new
     * {@link NodeGene} and has weight 1. Second new {@link ConnectionGene} goes from new {@link NodeGene} to old
     * {@link ConnectionGene} "to" {@link NodeGene} and has same weights as old {@link ConnectionGene}.
     * New {@link ConnectionGene}s are disabled in all genotypes.
     */
    public void splitConnection(ConnectionGene oldConnectionGene) {
        boolean[] allDisabled = Util.booleanArray(numOfGenotypes, false);
        NodeGene newNodeGene = new NodeGene(NeuronType.HIDDEN, allDisabled, neuronNames++);
        ConnectionGene firstConnectionGene = new ConnectionGene(oldConnectionGene.from, newNodeGene, Util.doubleArrayOfOnes(numOfGenotypes), allDisabled);
        ConnectionGene secondConnectionGene = new ConnectionGene(newNodeGene, oldConnectionGene.to, oldConnectionGene.weight, allDisabled);

        nodeGenes.add(newNodeGene);
        connectionGenes.add(firstConnectionGene);
        connectionGenes.add(secondConnectionGene);

        newNodeGene.enabled = allDisabled;
        firstConnectionGene.parent = oldConnectionGene;
        secondConnectionGene.parent = oldConnectionGene;
        oldConnectionGene.enabled = Util.booleanArray(numOfGenotypes, true);
        oldConnectionGene.firstChild = firstConnectionGene;
        oldConnectionGene.secondChild = secondConnectionGene;

        Collections.sort(nodeGenes);
        Collections.sort(connectionGenes);
    }

    /**
     * @return list of connections in gene pool that have split counterpart
     */
    public List<ConnectionGene> getSplitConnections() {
        return connectionGenes.stream()
                .filter((connectionGene -> connectionGene.firstChild != null))
                .collect(Collectors.toList());
    }

    /**
     * Takes previously split {@link ConnectionGene}, disabled old connection and activates its two child connection
     * and node.
     *
     * @param connectionGene which will be inactivated and its children activated
     * @param phenotype      for which split connection will be activated
     */
    public void activateSplitConnection(ConnectionGene connectionGene, int phenotype) {
        connectionGene.enabled[phenotype] = false;
        connectionGene.firstChild.enabled[phenotype] = true;
        connectionGene.secondChild.enabled[phenotype] = true;
        connectionGene.firstChild.to.enabled[phenotype] = true;
    }

    public void printConnections() {
        for (ConnectionGene connectionGene : connectionGenes) {
            System.out.println(connectionGene.from.name + " -> " + connectionGene.to.name);
        }
    }
}
