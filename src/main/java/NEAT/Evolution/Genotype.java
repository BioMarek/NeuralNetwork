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

public class Genotype implements Comparable<Genotype> {
    public final GenePool genePool;
    public List<ConnectionGene> connectionGenes;
    public List<NodeGene> nodeGenes;
    public Function<Double, Double> hiddenLayerActivationFunc;
    public Function<Double, Double> outputLayerActivationFunc;
    public int score = 0;

    public Genotype(GenePool genePool, List<NodeGene> nodeGenes, List<ConnectionGene> connectionGenes, Function<Double, Double> hiddenLayerActivationFunc, Function<Double, Double> outputLayerActivationFunc) {
        this.genePool = genePool;
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

    /**
     * Splits {@link ConnectionGene} into two new ones, the old {@link ConnectionGene} is removed. Weight of the first
     * {@link ConnectionGene} is 1 whereas weight of second one is same as weight of the old one.
     *
     * @param oldConnectionGene to remove and split
     */
    public void splitConnection(ConnectionGene oldConnectionGene) {
        NodeGene nodeGene = new NodeGene(NeuronType.HIDDEN, genePool.neuronNames++);
        ConnectionGene firstConnectionGene = new ConnectionGene(oldConnectionGene.from, nodeGene, 1.0, true);
        ConnectionGene secondConnectionGene = new ConnectionGene(nodeGene, oldConnectionGene.to, oldConnectionGene.weight, true);

        nodeGenes.add(nodeGene);
        connectionGenes.add(firstConnectionGene);
        connectionGenes.add(secondConnectionGene);
        connectionGenes.remove(oldConnectionGene);
        // TODO add new connections to genePool

        Collections.sort(nodeGenes);
        Collections.sort(connectionGenes);
    }

    public void mutateWeight(ConnectionGene connectionGene) {
        connectionGene.weight = Util.randomDouble();
    }

    public Genotype copy() {
        List<ConnectionGene> connectionGenesCopy = new ArrayList<>();
        connectionGenes.forEach(connectionGene -> connectionGenesCopy.add(connectionGene.copy()));
        Genotype genotype = new Genotype(genePool, nodeGenes, connectionGenesCopy, hiddenLayerActivationFunc, outputLayerActivationFunc);
        genotype.score = this.score;
        return genotype;
    }

    public void printConnections() {
        for (ConnectionGene connection : connectionGenes) {
            System.out.println(connection.from.name + " -> " + connection.to.name);
        }
    }

    @Override
    public int compareTo(Genotype genotype) {
        if (this.score == genotype.score)
            return 0;
        return this.score < genotype.score ? -1 : 1;
    }
}
