package NEAT.Evolution;

import BasicNeuralNetwork.NeuralNetwork.BasicNeuralNetwork;
import NEAT.NeuronType;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;
import Utils.Pair;
import Utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

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
    public void addConnection() {
        var allPossibleConnections = getPossibleConnections();

        if (allPossibleConnections.size() > 0) {
            Pair<NodeGene> chosen = allPossibleConnections.get(Util.randomInt(0, allPossibleConnections.size()));
            ConnectionGene connectionGene = new ConnectionGene(chosen.getFirst(), chosen.getSecond(), Util.randomDouble(), true);
            connectionGenes.add(connectionGene);
            genePool.putConnectionGeneIntoGenePool(connectionGene);
        }
    }

    /**
     * {@link ConnectionGene} can lead from {@link NodeGene} with lower name to {@link NodeGene} with higher name,
     * because node with lower name will be evaluated before it will receive output from node with higher name.
     * Connections between inputs and outputs are not allowed. Existing connection a backward connections are also
     * not allowed.
     *
     * @return {@link Pair}s of names of {@link NodeGene}s that can be connected.
     */
    public List<Pair<NodeGene>> getPossibleConnections() {
        List<Pair<NodeGene>> allExistingConnections = new ArrayList<>();
        connectionGenes.forEach(connectionGene -> {
            allExistingConnections.add(new Pair<>(connectionGene.from, connectionGene.to));
            allExistingConnections.add(new Pair<>(connectionGene.to, connectionGene.from));
        });

        List<Pair<NodeGene>> allPossibleConnections = new ArrayList<>();
        for (int from = 0; from < nodeGenes.size(); from++) {
            for (int to = from + 1; to < nodeGenes.size(); to++) {
                if (!((nodeGenes.get(from).type == NeuronType.INPUT && nodeGenes.get(to).type == NeuronType.INPUT) ||
                        (nodeGenes.get(from).type == NeuronType.OUTPUT && nodeGenes.get(to).type == NeuronType.OUTPUT)))
                    allPossibleConnections.add(new Pair<>(nodeGenes.get(from), nodeGenes.get(to)));
            }
        }
        allPossibleConnections.removeAll(allExistingConnections);

        return allPossibleConnections;
    }

    /**
     * With probability chanceToHardMutateWight assigns {@link ConnectionGene} new random weight or with probabilty
     * 1-chanceToHardMutateWight changes weight slightly. chanceToHardMutateWight is set in {@link GenePool}.
     *
     * @param connectionGene of which weight should be changed
     */
    public void mutateWeight(ConnectionGene connectionGene) {
        if (Util.randomChance(genePool.chanceToHardMutateWight)) {
            connectionGene.weight = Util.randomDouble();
        } else {
            connectionGene.weight += Util.randomDouble() * 0.2d;
            if (connectionGene.weight > 1.0d)
                connectionGene.weight = 1.0d;
            if (connectionGene.weight < -1.0d)
                connectionGene.weight = -1.0d;
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

    public void resetScore() {
        score = 0;
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
        connectionGenes.forEach(ConnectionGene::printConnectionGene);
    }

    @Override
    public int compareTo(Genotype genotype) {
        if (this.score == genotype.score)
            return 0;
        return this.score < genotype.score ? -1 : 1;
    }

    public void printScores() {
        System.out.print("\"" + name + "\" " + age + ": " + score + " | ");
    }

    /**
     * The function returns genotype which has reasonable performance in {@link BasicNeuralNetwork}, it is used as
     * benchmark against networks produced by {@link GenePool}.
     *
     * @return {@link Genotype} with same topology that proved to be reasonably good
     */
    public static Genotype referenceGenotype(GenePool genePool) {
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

    /**
     * The function returns genotype which has reasonable performance in {@link BasicNeuralNetwork}, it is used as
     * benchmark against networks produced by {@link GenePool}. Weights are set based on one particular
     * {@link BasicNeuralNetwork} with good performance.
     *
     * @return {@link Genotype} with same topology that proved to be reasonably good
     */
    public static Genotype workingGenotype(GenePool genePool) {
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

        double[][] firstLayerWeights = new double[][]{
                {0.8967, 0.8818, 0.4038, 0.7871, -0.5228, 0.5685, -0.7678, -0.2314},
                {0.6630, 0.8925, -0.1063, -0.0194, -0.5825, -0.7732, 0.4355, 0.9054},
                {-0.1175, -0.7909, -0.1552, 0.7718, -0.8304, 0.4782, 0.6288, 0.4052},
                {0.0135, -0.5693, -0.0430, 0.8312, 0.3774, 0.0804, 0.7065, -0.1919},
                {-0.5390, 0.3104, 0.0378, 0.6484, -0.0933, 0.9356, -0.9688, -0.0699},
                {0.6354, 0.6416, 0.5183, 0.0711, -0.2660, 0.8189, 0.0057, 0.5793},
                {0.6836, -0.8022, 0.4184, 0.0706, -0.7786, -0.4179, -0.5831, -0.2100},
                {0.3967, -0.4278, -0.4784, 0.8208, 0.4052, 0.5022, 0.8335, -0.2995},
        };
        double[][] outputLayerWeights = new double[][]{
                {0.0778, -0.2077, 0.1665, 0.0748, 0.4719, 0.3669, -0.0141, -0.3407},
                {0.0230, -0.2589, 0.5860, 0.7726, -0.7725, 0.8729, -0.5954, -0.0089},
                {0.3589, 0.2597, -0.5969, -0.7928, -0.7284, 0.4654, 0.3629, -0.7837},
                {0.2448, -0.9992, 0.1834, 0.3040, 0.9483, 0.4966, -0.8683, 0.5017}
        };

        for (int from = 0; from < 8; from++) {
            for (int to = 8; to < 16; to++) {
                genePool.putConnectionGeneIntoGenePool(inputNodes.get(from).name, inputNodes.get(to - 8).name);
                referenceConnectionGenes.add(new ConnectionGene(inputNodes.get(from), hiddenNodes.get(to - 8), firstLayerWeights[to - 8][from], true));
            }
        }
        for (int from = 8; from < 16; from++) {
            for (int to = 0; to < 4; to++) {
                genePool.putConnectionGeneIntoGenePool(inputNodes.get(from - 8).name, inputNodes.get(to).name);
                referenceConnectionGenes.add(new ConnectionGene(hiddenNodes.get(from - 8), outputNodes.get(to), outputLayerWeights[to][from - 8], true));
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
