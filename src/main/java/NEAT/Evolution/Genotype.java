package NEAT.Evolution;

import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

public class Genotype implements Comparable<Genotype> {
    public List<ConnectionGene> connectionGenes;
    public List<NodeGene> nodeGenes;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public int score = 0;

    public Genotype(List<NodeGene> nodeGenes, List<ConnectionGene> connectionGenes, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        this.connectionGenes = connectionGenes;
        this.nodeGenes = nodeGenes;
        this.hiddenLayerActivationFunc = hiddenLayerActivationFunc;
        this.outputLayerActivationFunc = outputLayerActivationFunc;
    }

    public Phenotype createPhenotype() {
        LinkedHashMap<Integer, NEATNeuron> neurons = new LinkedHashMap<>();
        List<Connection> connections = new ArrayList<>();

        nodeGenes.forEach((nodeGene) -> neurons.put(nodeGene.name, new NEATNeuron(nodeGene.name, nodeGene.type)));
        connectionGenes.forEach((connectionGene) -> connections.add(new Connection(
                neurons.get(connectionGene.from.name),
                neurons.get(connectionGene.to.name),
                connectionGene.weight)));
        return new Phenotype(new ArrayList<>(neurons.values()), connections, hiddenLayerActivationFunc, outputLayerActivationFunc);
    }

    public Genotype copy() {
        List<ConnectionGene> connectionGenesCopy = new ArrayList<>();
        connectionGenes.forEach(connectionGene -> connectionGenesCopy.add(connectionGene.copy()));
        Genotype genotype = new Genotype(nodeGenes, connectionGenesCopy, hiddenLayerActivationFunc, outputLayerActivationFunc);
        genotype.score = this.score;
        return genotype;
    }

    @Override
    public int compareTo(Genotype genotype) {
        if (this.score == genotype.score)
            return 0;
        return this.score < genotype.score ? -1 : 1;
    }
}
