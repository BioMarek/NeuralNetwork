package neat.evolution;

import lombok.EqualsAndHashCode;
import utils.Settings;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Species implements Comparable<Species> {
    public List<Genotype> genotypes;
    public int name;
    public int age = 0;
    public double average;

    public Species(List<Genotype> genotypes, int name) {
        this.genotypes = genotypes;
        this.name = name;
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
        int potentialReduction = Math.max((int) Math.floor(genotypes.size() * Settings.SPECIES_REDUCTION), Settings.MIN_SPECIES_REDUCTION);
        int actualReduction = Math.min(potentialReduction, genotypes.size());
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
        int limit = (int) Math.round(genotypes.size() * Settings.NETWORKS_TO_KEEP);
        for (int i = 0; i < genotypes.size(); i++) {
            // copies
            if (i < limit) {
                genotypes.get(i).age++;
                genotypesNewGeneration.add(genotypes.get(i));
            }
            // copies with one mutation
            if (i >= limit) {
                Genotype genotype = genotypes.get(i - limit).copy();
                genotype.mutateGenotype();
                genotypesNewGeneration.add(genotype);
            }
            // TODO crossingOvers
        }
        genotypes = genotypesNewGeneration;
    }

    public int getSize() {
        return genotypes.size();
    }

    public void increaseAge() {
        age++;
    }

    public void mutateAndAge() {
        mutateSpecies();
        increaseAge();
    }

    /**
     * Checks whether {@link Species} does contain no {@link Genotype} and is therefore extinct.
     *
     * @return true if {@link Species} contains no genotypes, false otherwise.
     */
    public boolean isExtinct() {
        return genotypes.size() == 0;
    }

    @Override
    public int compareTo(Species species) {
        if (this.average == species.average)
            return 0;
        return (this.average < species.average) ? -1 : 1;
    }
}
