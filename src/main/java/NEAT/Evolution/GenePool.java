package NEAT.Evolution;

import NEAT.NeuronType;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;
import Utils.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenePool {
    private int maxNeurons = 1000;
    private int numOfGenotypes = 100;
    private int neuronNames = 0;
    public List<ConnectionGene> connectionGenes = new ArrayList<>();
    public List<NodeGene> nodeGenes = new ArrayList<>();
    public List<Phenotype> phenotypes = new ArrayList<>();

    public void initGenePool(int inputs, int outputs) {
        List<NodeGene> inputNodes = new ArrayList<>();
        List<NodeGene> outputNodes = new ArrayList<>();
        for (int i = 0; i < inputs; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.INPUT, neuronNames++);
            inputNodes.add(nodeGene);
            nodeGenes.add(nodeGene);
        }
        for (int i = 0; i < outputs; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.OUTPUT, maxNeurons--);
            outputNodes.add(nodeGene);
            nodeGenes.add(nodeGene);
        }
        for (NodeGene input : inputNodes) {
            for (NodeGene output : outputNodes) {
                connectionGenes.add(new ConnectionGene(input, output, Util.randomDoubleArray(numOfGenotypes), Util.booleanArray(numOfGenotypes, true)));
            }
        }
    }

    public void createPhenotypes() {
        for (int i = 0; i < numOfGenotypes; i++) {
            LinkedHashMap<Integer, NEATNeuron> neurons = new LinkedHashMap<>();

            nodeGenes.forEach((nodeGene) -> neurons.put(nodeGene.name, new NEATNeuron(nodeGene.name, nodeGene.type)));
            Phenotype phenotype = new Phenotype(new ArrayList<>(neurons.values()));

            for (ConnectionGene connectionGene : connectionGenes) {
                phenotype.connections.add(new Connection(
                        neurons.get(connectionGene.from.name),
                        neurons.get(connectionGene.to.name),
                        connectionGene.weight[i]));
            }
            phenotypes.add(phenotype);
        }
    }
}
