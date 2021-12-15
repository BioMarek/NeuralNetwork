package NEAT.Evolution;

import NEAT.NeuronType;
import NEAT.Phenotype.Connection;
import NEAT.Phenotype.NEATNeuron;
import NEAT.Phenotype.Phenotype;
import Utils.Pair;
import Utils.Util;

import java.util.*;
import java.util.stream.Collectors;

// TODO try to reduce number of sorts
// TODO consolidate print and toString functions
public class Genotype implements Comparable<Genotype> {
    public final GenePool genePool;
    public List<ConnectionGene> connectionGenes = new ArrayList<>();
    public List<NodeGene> nodeGenes = new ArrayList<>();
    public List<NodeGene> inputNodes = new ArrayList<>();
    public int neuronNames = 0;
    public int maxNeurons;
    public int score = 0;
    public String name = "0";
    public int age = 0;

    public Genotype(GenePool genePool) {
        this.genePool = genePool;
    }

    public Genotype(GenePool genePool, int inputs, int outputs) {
        this.genePool = genePool;
        maxNeurons = genePool.maxNeurons;
        List<NodeGene> outputNodes = new ArrayList<>();

        for (int i = 0; i < inputs; i++) {
            inputNodes.add(new NodeGene(NeuronType.INPUT, neuronNames++, 0));
        }
        for (int i = 0; i < outputs; i++) {
            outputNodes.add(new NodeGene(NeuronType.OUTPUT, maxNeurons--, 1000));
        }
        for (NodeGene input : inputNodes) {
            for (NodeGene output : outputNodes) {
                ConnectionGene connectionGene = new ConnectionGene(input, output, Util.randomDouble(), true);
                input.connectionGenes.add(connectionGene);
                Collections.sort(input.connectionGenes);
                connectionGenes.add(connectionGene);
            }
        }
        nodeGenes.addAll(inputNodes);
        nodeGenes.addAll(outputNodes);

        Collections.sort(inputNodes);
        Collections.sort(nodeGenes);
        Collections.sort(connectionGenes);
        name = Integer.toString(genePool.networksGenerated++);
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

    public void addNode() {
        addNode(getRandomConnection());
    }

    /**
     * Splits {@link ConnectionGene} into two new ones, the old {@link ConnectionGene} is removed. Weight of the first
     * {@link ConnectionGene} is 1 whereas weight of second one is same as weight of the old one.
     *
     * @param connectionGene to remove and split
     */
    public void addNode(ConnectionGene connectionGene) {
        NodeGene nodeGene = new NodeGene(NeuronType.HIDDEN, neuronNames++, connectionGene.from.layer + 1);

        ConnectionGene firstConnectionGene = new ConnectionGene(connectionGene.from, nodeGene, 1.0, true);
        ConnectionGene secondConnectionGene = new ConnectionGene(nodeGene, connectionGene.to, connectionGene.weight, true);
        nodeGene.connectionGenes.add(secondConnectionGene);

        nodeGenes.add(nodeGene);
        connectionGenes.add(firstConnectionGene);
        connectionGenes.add(secondConnectionGene);

        connectionGene.from.connectionGenes.add(firstConnectionGene);
        connectionGene.from.connectionGenes.remove(connectionGene);
        connectionGenes.remove(connectionGene);
        updateLayerNumbers(secondConnectionGene);

        Collections.sort(nodeGenes);
        Collections.sort(connectionGenes);
    }

    // TODO test this
    public void updateLayerNumbers(ConnectionGene connection) {
        NodeGene targetNode = connection.to;
        if (targetNode.layer <= connection.from.layer) {
            targetNode.layer++;
            for (ConnectionGene connectionGene : targetNode.connectionGenes) {
                updateLayerNumbers(connectionGene);
            }
        }
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
            updateLayerNumbers(connectionGene);
            Collections.sort(connectionGenes);
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
     * Creates deep copy of {@link Genotype}. Copy has same topology of {@link NodeGene}s and {@link Connection}s as
     * original and all values are same.
     *
     * @return copied {@link Genotype} object
     */
    public Genotype copy() {
        Genotype genotype = new Genotype(genePool);

        genotype.nodeGenes = nodeGenes.stream()
                .map(NodeGene::copy)
                .collect(Collectors.toList());
        genotype.inputNodes = genotype.nodeGenes.stream()
                .filter(nodeGene -> nodeGene.type == NeuronType.INPUT)
                .collect(Collectors.toList());

        Map<Integer, NodeGene> nodeGeneMap = new HashMap<>();
        genotype.nodeGenes.forEach(nodeGene -> nodeGeneMap.put(nodeGene.name, nodeGene));
        for (ConnectionGene connectionGene : connectionGenes) {
            ConnectionGene newConnectionGene = new ConnectionGene(
                    nodeGeneMap.get(connectionGene.from.name),
                    nodeGeneMap.get(connectionGene.to.name),
                    connectionGene.weight,
                    connectionGene.enabled);
            genotype.connectionGenes.add(newConnectionGene);
            nodeGeneMap.get(connectionGene.from.name).connectionGenes.add(newConnectionGene);
        }

        genotype.neuronNames = neuronNames;
        genotype.name = Integer.toString(genePool.networksGenerated++);
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
     * In copy wa want two genotypes to have same node names and connections going from node with same node into node
     * with same name. But Objects itself cannot be same because it is hard copy, therefore no Object.equals().
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return neuronNames == genotype.neuronNames && score == genotype.score && age == genotype.age && Objects.equals(genePool, genotype.genePool) && connectionGenes.equals(genotype.connectionGenes) && nodeGenes.equals(genotype.nodeGenes) && inputNodes.equals(genotype.inputNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genePool, connectionGenes, nodeGenes, inputNodes, neuronNames, score, age);
    }
}
