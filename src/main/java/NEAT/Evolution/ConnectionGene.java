package NEAT.Evolution;

public class ConnectionGene {
    public NodeGene from;
    public NodeGene to;
    public double[] weight;
    public boolean[] enabled;

    public ConnectionGene(NodeGene from, NodeGene to, double[] weight, boolean[] enabled) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.enabled = enabled;
    }

    public int from(){
        return from.name;
    }

    public int to(){
        return to.name;
    }
}
