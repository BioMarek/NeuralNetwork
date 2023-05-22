package games.freeEvolution;

import neat.NeuronType;
import neat.evolution.ConnectionGene;
import neat.evolution.GenePool;
import neat.evolution.Genotype;
import neat.evolution.NodeGene;
import neat.phenotype.Connection;
import neat.phenotype.NEATNeuron;
import neat.phenotype.Phenotype;
import utils.Pair;
import utils.Settings;
import utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static utils.Util.isRandomChanceTrue;
import static utils.Util.randomDouble;
import static utils.Util.randomInt;
import static utils.Util.repeat;

public class FEGenotype implements Comparable<Genotype> {
    public static int names = 0;
    public List<ConnectionGene> connectionGenes = new ArrayList<>();
    public List<NodeGene> nodeGenes = new ArrayList<>();
    public List<NodeGene> inputNodes = new ArrayList<>();
    public int neuronNames = 0;
    public int maxNeurons;
    public int score = 0;
    public int name ;
    public int age = 0;

    public FEGenotype() {
    }

    public FEGenotype(int inputs, int outputs) {
        maxNeurons = Settings.MAX_NEURONS - outputs + 1;
        List<NodeGene> outputNodes = new ArrayList<>();

        repeat.accept(inputs, () -> inputNodes.add(new NodeGene(NeuronType.INPUT, neuronNames++, 0)));
        repeat.accept(outputs, () -> outputNodes.add(new NodeGene(NeuronType.OUTPUT, maxNeurons++, Integer.MAX_VALUE)));

        for (NodeGene input : inputNodes) {
            for (NodeGene output : outputNodes) {
                ConnectionGene connectionGene = new ConnectionGene(input, output, randomDouble(), true);
                input.connectionGenes.add(connectionGene);
                connectionGenes.add(connectionGene);
            }
        }

        nodeGenes.addAll(inputNodes);
        nodeGenes.addAll(outputNodes);
        name = names++;
    }


    /**
     * Translates {@link Genotype} to {@link Phenotype}.
     *
     * @return {@link Phenotype} corresponding to this {@link Genotype}
     */
    public Phenotype createPhenotype() {
        LinkedHashMap<Integer, NEATNeuron> neurons = new LinkedHashMap<>();
        List<Connection> connections = new ArrayList<>();

        nodeGenes.forEach((nodeGene) -> neurons.put(nodeGene.name, new NEATNeuron(nodeGene.name, nodeGene.layer, nodeGene.type)));
        connectionGenes.stream()
                .filter(connectionGene -> connectionGene.enabled)
                .forEach((connectionGene) -> connections.add(new Connection(
                        neurons.get(connectionGene.from.name),
                        neurons.get(connectionGene.to.name),
                        connectionGene.weight)));
        return new Phenotype(new ArrayList<>(neurons.values()), connections);
    }

    /**
     * Mutates {@link Genotype} with chances set in {@link GenePool}.
     */
    public void mutateGenotype() {
        if (isRandomChanceTrue(Settings.CHANCE_SWITCH_CONNECTION_ENABLED))
            switchConnectionEnabled(getRandomConnection());
        if (isRandomChanceTrue(Settings.CHANCE_MUTATE_WEIGHT))
            mutateWeight(getRandomConnection());
        if (isRandomChanceTrue(Settings.CHANCE_ADD_CONNECTION))
            addConnection();
    }

    public void addNode() {
        addNode(getRandomConnection());
    }

    /**
     * Splits {@link ConnectionGene} into two new ones, the old {@link ConnectionGene} is removed. Weight of the first
     * {@link ConnectionGene} is 1 whereas weight of second one is same as weight of the old one. So from x -> y we will
     * have x -> z and z -> y, z is newly added {@link NodeGene}. Layer of y is increased by one and layer of all n
     * such as y -> n are updated accordingly.
     *
     * @param connectionGene to remove and split
     */
    public void addNode(ConnectionGene connectionGene) {
        NodeGene nodeGene = new NodeGene(NeuronType.HIDDEN, neuronNames++, connectionGene.from.layer + 1);

        ConnectionGene firstConnectionGene = new ConnectionGene(connectionGene.from, nodeGene, 1.0d, true);
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

    /**
     * Recalculates layers of {@link NodeGene}s if needed. When {@link ConnectionGene} is added its destination
     * {@link NodeGene} has to be on higher layer than its source {@link NodeGene}. By inserting new {@link NodeGene}
     * and {@link ConnectionGene} old {@link NodeGene} on upper layer which are connected are pushed up by one layer.
     * That way in {@link Phenotype} {@link NEATNeuron}s can be updated from lower layers to upper ones deterministically.
     *
     * @param connection that was added and which destination {@link NodeGene} layer should be recalculated
     */
    public void updateLayerNumbers(ConnectionGene connection) {
        NodeGene targetNode = connection.to;
        if (targetNode.layer <= connection.from.layer) {
            targetNode.layer++;
            targetNode.connectionGenes.forEach(this::updateLayerNumbers);
        }
    }

    /**
     * Connects two, so far, unconnected nodes with new {@link ConnectionGene}, and updates layer numbers if necessary.
     */
    public void addConnection() {
        var allPossibleConnections = getPossibleConnections();

        if (allPossibleConnections.size() > 0) {
            Pair<NodeGene> chosen = allPossibleConnections.get(randomInt(0, allPossibleConnections.size()));
            ConnectionGene connectionGene = new ConnectionGene(chosen.getFirst(), chosen.getSecond(), randomDouble(), true);
            chosen.getFirst().connectionGenes.add(connectionGene);
            connectionGenes.add(connectionGene);
            updateLayerNumbers(connectionGene);
            Collections.sort(connectionGenes);
        }
    }

    /**
     * {@link ConnectionGene} can lead from {@link NodeGene} on lower layer to {@link NodeGene} on higher layer,
     * because {@link NodeGene}s on lower layers will be evaluated before {@link NodeGene} on higher layer will need be
     * evaluated. Connections between inputs and outputs are not allowed. Existing connection a backward connections
     * are also not allowed.
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
     * With probability chanceToHardMutateWight assigns {@link ConnectionGene} new random weight or with probability
     * 1 - chanceToHardMutateWight changes weight slightly. chanceToHardMutateWight is set in {@link GenePool}.
     *
     * @param connectionGene of which weight should be changed
     */
    public void mutateWeight(ConnectionGene connectionGene) {
        if (isRandomChanceTrue(Settings.CHANCE_HARD_MUTATE_WEIGHT)) {
            connectionGene.weight = randomDouble();
        } else {
            connectionGene.weight += randomDouble() * 0.2d;
            if (connectionGene.weight > 1.0d)
                connectionGene.weight = 1.0d;
            if (connectionGene.weight < -1.0d)
                connectionGene.weight = -1.0d;
        }
    }

    /**
     * Switches enabled field of {@link ConnectionGene}. It is possible to disable all {@link ConnectionGene} coming
     * to or from {@link NodeGene}.
     *
     * @param connectionGene which should be enabled/disabled
     */
    public void switchConnectionEnabled(ConnectionGene connectionGene) {
        connectionGene.enabled = !connectionGene.enabled;
    }

    /**
     * Chooses one {@link ConnectionGene} randomly from {@link Genotype} connections.
     *
     * @return picked {@link ConnectionGene}
     */
    public ConnectionGene getRandomConnection() {
        return connectionGenes.get(randomInt(0, connectionGenes.size()));
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score = 0;
    }

    /**
     * Creates deep copy of {@link Genotype}. Copy has same topology of {@link NodeGene}s and {@link Connection}s as
     * original and all values are same. But all object in copy are created as new ones.
     *
     * @return copied {@link Genotype} object
     */
    public FEGenotype copy() {
        FEGenotype genotype = new FEGenotype();

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
        genotype.name = names++;
        return genotype;
    }

    public FEGenotype getMutatedCopy(FEGenotype feGenotype) {
        FEGenotype genotype = feGenotype.copy();
        genotype.mutateGenotype();

        if (isRandomChanceTrue(Settings.CHANCE_ADD_NODE))
            genotype.addNode();

        return genotype;
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
        FEGenotype genotype = (FEGenotype) o;
        return neuronNames == genotype.neuronNames && score == genotype.score && age == genotype.age && connectionGenes.equals(genotype.connectionGenes) && nodeGenes.equals(genotype.nodeGenes) && inputNodes.equals(genotype.inputNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionGenes, nodeGenes, inputNodes, neuronNames, score, age);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        connectionGenes.forEach((connectionGene -> result.append(connectionGene.toString())));
        return result.toString();
    }
}
