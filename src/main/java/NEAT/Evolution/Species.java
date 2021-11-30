package NEAT.Evolution;

import NEAT.Phenotype.Phenotype;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode
public class Species implements Comparable<Species> {
    private final GenePool genePool;
    public List<Genotype> genotypes;
    public int name;
    public int age = 0;
    public double average;

    public Species(GenePool genePool, List<Genotype> genotypes, int name) {
        this.genePool = genePool;
        this.genotypes = genotypes;
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

    /**
     * Calculates average score os {@link Species} from scores of {@link Genotype}.
     */
    public void calculateAverage() {
        average = (double) genotypes.stream()
                .map(Genotype::getScore)
                .reduce(0, Integer::sum) / genotypes.size();
    }

    /**
     * Reduces size of species. And removes genotypes that are at the last positions.
     *
     * @return returns number of removed genotypes.
     */
    public int reduceSize() {
        int reduction = Math.max((int) Math.floor(genotypes.size() * genePool.speciesReduction), genePool.speciesMinimalReduction);
        int actualReduction = Math.min(reduction, genotypes.size());
        int oldSize = genotypes.size();
        for (int i = 0; i < actualReduction; i++) {
            genotypes.remove(oldSize - 1 - i);
        }
        return actualReduction;
    }

    /**
     * Increases number of {@link Genotype}s by given number. New nth genotype is mutated copy of nth {@link Genotype} of {@link Species}.
     *
     * @param increase how many new {@link Genotype}s should be added
     */
    public void increaseSize(int increase) {
        for (int i = 0; i < increase; i++) {
            Genotype newGenotype = genotypes.get(i).copy();
            newGenotype.mutateGenotype();
            genotypes.add(newGenotype);
        }
    }


    public void mutateSpecies() {
        List<Genotype> genotypesNewGeneration = new ArrayList<>();
        System.out.println("genotypes: " + genotypes.size());
        for (int i = 0; i < genotypes.size(); i++) {
            // copies
            int limit = (int) Math.round(genotypes.size() * genePool.networksToKeep);
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

    public int getSize() {
        return genotypes.size();
    }

    public void increaseAge() {
        age++;
    }

    @Override
    public int compareTo(Species species) {
        if (this.average == species.average)
            return 0;
        return (this.average < species.average) ? -1 : 1;
    }
}