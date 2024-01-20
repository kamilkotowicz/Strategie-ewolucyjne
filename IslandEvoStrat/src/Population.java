import java.util.*;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

public class Population {
    private TreeSet<Individual> individuals;

    public Population() {
        this.individuals = new TreeSet<>(Comparator.comparingDouble(a -> a.fitness));
    }

    public void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    public Individual getIndividualWithLargestFitness() {
        return individuals.last();
    }

    public Individual getIndividualWithSmallestFitness() {
        return individuals.first();
    }

    public Individual getRandomIndividual() {
        int randomIndex = new Random().nextInt(individuals.size());
        return individuals.stream().skip(randomIndex).findFirst().orElse(null);
    }

    public List<Individual> getKIndividualsWithLargestFitness(int k) {
        List<Individual> result = new ArrayList<>();
        Iterator<Individual> iterator = individuals.descendingIterator();

        for (int i = 0; i < k && iterator.hasNext(); i++) {
            result.add(iterator.next());
        }

        return result;
    }

    public List<Individual> getKIndividualsWithSmallestFitness(int k) {
        List<Individual> result = new ArrayList<>();
        Iterator<Individual> iterator = individuals.iterator();

        for (int i = 0; i < k && iterator.hasNext(); i++) {
            result.add(iterator.next());
        }

        return result;
    }

    public List<Individual> getKRandomIndividuals(int k) {
        List<Individual> result = new ArrayList<>(individuals);
        List<Individual> randomIndividuals = new ArrayList<>(k);

        Random random = new Random();
        while (randomIndividuals.size() < k && !result.isEmpty()) {
            int randomIndex = random.nextInt(result.size());
            randomIndividuals.add(result.remove(randomIndex));
        }

        return randomIndividuals;
    }
    public void removeIndividual(Individual individual) {
        individuals.remove(individual);
    }

    public void initializePopulation(final int POPULATION_SIZE, final int DIMENSIONS){
        for(int i = 0; i < POPULATION_SIZE; ++i){
            Individual random_individual = Individual.createRandomIndividual(DIMENSIONS);
            addIndividual(random_individual);
        }
    }

    public void nextGeneration(final int NUM_OF_CHILDREN, final int DIMENSIONS) {
        final double MUTATION_RATE = 0.1;
        double tau0 = 1.0 / sqrt(2.0 * DIMENSIONS);
        double tau1 = 1.0 / sqrt(2.0 * sqrt(DIMENSIONS));

        List<Individual> copyIndividuals = new ArrayList<>(individuals);
        individuals.clear();

        Random random = new Random();

        for(int i = 0; i < NUM_OF_CHILDREN; ++i){
            int parentIndex = random.nextInt(copyIndividuals.size());
            Individual parent = copyIndividuals.get(parentIndex);

            Individual child = new Individual(parent);

            for(int j = 0; j < DIMENSIONS; ++j){
                if(random.nextDouble() < MUTATION_RATE){
                    child.mutationSteps[j] *= exp(tau0 * random.nextGaussian() + tau1 * random.nextGaussian());
                    child.coords[j] += random.nextGaussian() * child.mutationSteps[j];
                }
            }
            child.fitness = Main.calculate_fitness(child.coords);

            individuals.add(child);
        }

    }

    public void keepSurvivors(final int POPULATION_SIZE) {
        if (individuals.size() <= POPULATION_SIZE) {
            return;
        }

        Individual smallestFitness = getIndividualWithSmallestFitness();

        while (individuals.size() > POPULATION_SIZE && smallestFitness != null) {
            removeIndividual(smallestFitness);
            smallestFitness = getIndividualWithSmallestFitness();
        }
    }

}

