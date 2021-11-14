package NEAT.Evolution;

import NEAT.NeuronType;

public class NodeGene {
    public NeuronType type;
    public int name;

    public NodeGene(NeuronType type, int name) {
        this.type = type;
        this.name = name;
    }
}
