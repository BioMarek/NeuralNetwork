package NEAT.Evolution;

import BasicNeuralNetwork.NeuralNetwork.BasicNeuralNetwork;
import NEAT.NeuronType;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;
import Utils.Pair;
import Utils.Util;

import java.util.*;
import java.util.function.Function;

public class Genotype implements Comparable<Genotype> {
    public final GenePool genePool;
    public List<ConnectionGene> connectionGenes;
    public List<NodeGene> nodeGenes;
    public int score = 0;
    public String name = "0";
    int age = 0;

    public Genotype(GenePool genePool, List<NodeGene> nodeGenes, List<ConnectionGene> connectionGenes) {
        this.genePool = genePool;
        this.connectionGenes = connectionGenes;
        this.nodeGenes = nodeGenes;
    }

    /**
     * Translates {@link Genotype} to {@link Phenotype}.
     *
     * @return {@link Phenotype} corresponding to this {@link Genotype}
     */
    public Phenotype createPhenotype() {
        LinkedHashMap<Integer, NEATNeuron> neurons = new LinkedHashMap<>();
        List<Connection> connections = new ArrayList<>();

        nodeGenes.forEach((nodeGene) -> neurons.put(nodeGene.name, new NEATNeuron(nodeGene.name, nodeGene.type)));
        connectionGenes.forEach((connectionGene) -> connections.add(new Connection(
                neurons.get(connectionGene.from.name),
                neurons.get(connectionGene.to.name),
                connectionGene.weight)));
        return new Phenotype(genePool, new ArrayList<>(neurons.values()), connections);
    }

    /**
     * Mutates {@link Genotype} with chances set in {@link GenePool}.
     */
    public void mutateGenotype() {
        if (Util.randomChance(genePool.chanceToMutateWeight))
            mutateWeight(getRandomConnection());
        if (Util.randomChance(genePool.chanceToAddConnection))
            addConnection();
    }

    /**
     * Splits {@link ConnectionGene} into two new ones, the old {@link ConnectionGene} is removed. Weight of the first
     * {@link ConnectionGene} is 1 whereas weight of second one is same as weight of the old one.
     *
     * @param ConnectionGene to remove and split
     */
    public void addNode(ConnectionGene ConnectionGene) {
        NodeGene nodeGene = new NodeGene(NeuronType.HIDDEN, genePool.nodeNameOfSplitConnection(ConnectionGene));

        ConnectionGene firstConnectionGene = new ConnectionGene(ConnectionGene.from, nodeGene, 1.0, true);
        ConnectionGene secondConnectionGene = new ConnectionGene(nodeGene, ConnectionGene.to, ConnectionGene.weight, true);

        nodeGenes.add(nodeGene);
        connectionGenes.add(firstConnectionGene);
        connectionGenes.add(secondConnectionGene);
        connectionGenes.remove(ConnectionGene);

        genePool.putConnectionGeneIntoGenePool(firstConnectionGene);
        genePool.putConnectionGeneIntoGenePool(secondConnectionGene);

        Collections.sort(nodeGenes);
        Collections.sort(connectionGenes);
    }

    public void addNode() {
        addNode(getRandomConnection());
    }

    /**
     * Connects two, so far, unconnected nodes with new {@link ConnectionGene}.
     */
    void addConnection() {
        Set<Pair<NodeGene>> allPossibleConnections = new HashSet<>();
        for (NodeGene nodeGeneFrom : nodeGenes) {
            for (NodeGene nodeGeneTo : nodeGenes) {
                if (!(nodeGeneFrom.type == NeuronType.INPUT && nodeGeneTo.type == NeuronType.OUTPUT))
                    allPossibleConnections.add(new Pair<>(nodeGeneFrom, nodeGeneTo));
            }
        }

        if (allPossibleConnections.size() > 0) {
            List<Pair<NodeGene>> allPossibleConnectionsList = new ArrayList<>(allPossibleConnections);
            Pair<NodeGene> chosen = allPossibleConnectionsList.get(Util.randomInt(0, allPossibleConnectionsList.size()));
            ConnectionGene connectionGene = new ConnectionGene(chosen.getFirst(), chosen.getSecond(), Util.randomDouble(), true);
            connectionGenes.add(connectionGene);
            genePool.putConnectionGeneIntoGenePool(connectionGene);
        }
    }

    /**
     * With probability chanceToHardMutateWight assigns {@link ConnectionGene} new random weight or with probabilty
     * 1-chanceToHardMutateWight changes weight slightly. chanceToHardMutateWight is set in {@link GenePool}.
     *
     * @param connectionGene of which weight should be changed
     */
    public void mutateWeight(ConnectionGene connectionGene) {
        if (Util.randomChance(genePool.chanceToHardMutateWight)) {
//            System.out.print(connectionGene.from.name + " -> " + connectionGene.to.name + " old weight: " + connectionGene.weight);
            connectionGene.weight = Util.randomDouble();
//            System.out.println(" new weight " + connectionGene.weight);
        } else {
            connectionGene.weight += Util.randomDouble() * 0.2;
            if (connectionGene.weight > 1.0)
                connectionGene.weight = 1.0;
            if (connectionGene.weight < -1.0)
                connectionGene.weight = -1.0;
        }
    }

    /**
     * Chooses one {@link ConnectionGene} randomly from {@link Genotype} connections.
     *
     * @return picked {@link ConnectionGene}
     */
    public ConnectionGene getRandomConnection() {
        return connectionGenes.get(Util.randomInt(0, connectionGenes.size()));
    }

    public int getScore() {
        return score;
    }

    /**
     * Creates deep copy of {@link Genotype}. Only {@link ConnectionGene}s are deep copied other variables are referenced
     * as they don't change.
     *
     * @return copied {@link Genotype} object
     */
    public Genotype copy() {
        List<ConnectionGene> connectionGenesCopy = new ArrayList<>();
        connectionGenes.forEach(connectionGene -> connectionGenesCopy.add(connectionGene.copy()));
        Genotype genotype = new Genotype(genePool, nodeGenes, connectionGenesCopy);
        genotype.score = this.score;
        genotype.name = Integer.toString(genePool.networksGenerated++);
        genotype.age = 0;
        return genotype;
    }

    public void printConnections() {
        connectionGenes.forEach((connectionGene -> System.out.printf("%-3d -> %-4d %7.4f%n", connectionGene.from.name, connectionGene.to.name, connectionGene.weight)));
    }

    @Override
    public int compareTo(Genotype genotype) {
        if (this.score == genotype.score)
            return 0;
        return this.score < genotype.score ? -1 : 1;
    }

    /**
     * The function returns genotype which has reasonable performance in {@link BasicNeuralNetwork}, it is used as benchmark
     * against networks produced by {@link GenePool}.
     *
     * @return {@link Genotype} with same topology that proved to be reasonably good
     */
    public static Genotype referenceGenotype(GenePool genePool, Function<Double, Double> hiddenLayerActivationFunc,
                                             Function<Double, Double> outputLayerActivationFunc) {
        List<NodeGene> inputNodes = new ArrayList<>();
        List<NodeGene> hiddenNodes = new ArrayList<>();
        List<NodeGene> outputNodes = new ArrayList<>();
        List<ConnectionGene> referenceConnectionGenes = new ArrayList<>();
        int maxNeurons = 1000;

        for (int i = 0; i < 8; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.INPUT, genePool.neuronNames++);
            inputNodes.add(nodeGene);
        }
        for (int i = 0; i < 8; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.HIDDEN, genePool.neuronNames++);
            hiddenNodes.add(nodeGene);
        }
        for (int i = 0; i < 4; i++) {
            NodeGene nodeGene = new NodeGene(NeuronType.OUTPUT, maxNeurons--);
            outputNodes.add(nodeGene);
        }
        for (NodeGene input : inputNodes) {
            for (NodeGene output : hiddenNodes) {
                genePool.putConnectionGeneIntoGenePool(input.name, output.name);
                referenceConnectionGenes.add(new ConnectionGene(input, output, Util.randomDouble(), true));
            }
        }
        for (NodeGene input : hiddenNodes) {
            for (NodeGene output : outputNodes) {
                genePool.putConnectionGeneIntoGenePool(input.name, output.name);
                referenceConnectionGenes.add(new ConnectionGene(input, output, Util.randomDouble(), true));
            }
        }

        inputNodes.addAll(hiddenNodes);
        inputNodes.addAll(outputNodes);

        Collections.sort(inputNodes);
        Collections.sort(referenceConnectionGenes);
        Genotype genotype = new Genotype(genePool, inputNodes, referenceConnectionGenes);
        genotype.name = Integer.toString(-1);

        return genotype;
    }
}
