package NEAT.Evolution;

import NEAT.Phenotype.Phenotype;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
public class Species implements Comparable<Species> {
    public GenePool genePool;
    public List<Genotype> genotypes;
    int name;
    public int size;
    public int age = 0;
    public double average;

    public Species(GenePool genePool, List<Genotype> genotypes, int name) {
        this.genePool = genePool;
        this.genotypes = genotypes;
        this.size = genotypes.size();
        this.name = name;
    }

    public void calculateScores() {
        for (Genotype genotype : genotypes) {
            for (int i = 0; i < genePool.numOfTrials; i++) {
                Phenotype phenotype = genotype.createPhenotype();
                genePool.game.reset();
                genotype.score += genePool.game.play(phenotype, genePool.maxNumberOfMoves);
            }
        }
        genotypes.sort(Collections.reverseOrder());
    }

    public void calculateAverage() {
        double sum = 0.0;
        for (Genotype genotype : genotypes) {
            sum += genotype.score;
        }
        average = sum / size;
    }

    /**
     * Reduces size of species. And removes genotypes that are at the last positions.
     *
     * @return returns number of removed genotypes.
     */
    public int reduceSize() {
        int reduction = Math.max((int) Math.floor(size * genePool.speciesReduction), genePool.speciesMinimalReduction);
        int actualReduction = Math.min(reduction, size);
        size -= actualReduction;
        for (int i = 0; i < actualReduction; i++){
            genotypes.remove(actualReduction - 1 - i);
        }

        return actualReduction;
    }

    public void increaseSize(int increase){
        this.size += increase;
        for (int i = 0; i < increase; i++){
            Genotype newGenotype = genotypes.get(i).copy();
            newGenotype.mutateGenotype();
            genotypes.add(newGenotype);
        }
    }


    public void mutateSpecies() {
        List<Genotype> genotypesNewGeneration = new ArrayList<>();
        System.out.println("size: " + size + " genotypes: " + genotypes.size());
        for (int i = 0; i < size; i++) {
            // copies
            int limit = (int) Math.round(size * genePool.networksToKeep);
            if (i < limit) {
                genotypesNewGeneration.add(genotypes.get(i));
            }
            // copies with one mutation
            if (i >= limit) {
                Genotype genotype = genotypes.get(i - limit).copy();
                genotype.mutateGenotype();
                genotype.name = Integer.toString(genePool.networksGenerated++);
                genotypesNewGeneration.add(genotype);
            }
            // crossingOvers
        }
        genotypes = genotypesNewGeneration;
    }

    @Override
    public int compareTo(Species species) {
        if (this.average == species.average)
            return 0;
        return (this.average < species.average) ? -1 : 1;
    }
}
