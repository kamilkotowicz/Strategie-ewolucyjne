import java.util.Arrays;
import java.util.Random;

public class Individual{
    public double[] coords;
    public double[] mutationSteps;
    public double fitness;

    public static Individual createRandomIndividual(final int DIMENSIONS){
        final double INITIAL_MUTATION_STEP = 1.0;

        Random rand = new Random();
        Individual individual = new Individual();

        individual.coords = new double[DIMENSIONS];
        individual.mutationSteps = new double[DIMENSIONS];

        for(int i = 0; i < DIMENSIONS; ++i){
            individual.coords[i] = rand.nextDouble() * 10;
        }

        for(int i = 0; i < DIMENSIONS; ++i){
            individual.mutationSteps[i] = INITIAL_MUTATION_STEP;
        }

        individual.fitness = Main.calculate_fitness(individual.coords);
        return individual;
    }

    public Individual(){
        fitness = Double.NEGATIVE_INFINITY;
    }

    public Individual(Individual other) {
        this.coords = Arrays.copyOf(other.coords, other.coords.length);
        this.mutationSteps = Arrays.copyOf(other.mutationSteps, other.mutationSteps.length);
        this.fitness = other.fitness;
    }
}