import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.exp;
import static java.lang.Math.sqrt;

public class EvolutionaryAlgorithm {

    private int POPULATION_SIZE;
    private int CHILD_SIZE;
    private int ITERATIONS;
    private static final double MUTATION_RATE = 0.1;
    private static final double INITIAL_MUTATION_STEP = 1;
    private int DIMENSION;

    private double[][] population_coords;
    private double[][] population_mutation_steps;

    private double[][] children_coords;

    private double[][] children_mutation_steps;

    public EvolutionaryAlgorithm(int POPULATION_SIZE, int DIMENSION, int ITERATIONS, int CHILD_SIZE){
        this.POPULATION_SIZE = POPULATION_SIZE;
        this.DIMENSION = DIMENSION;
        this.ITERATIONS = ITERATIONS;
        this.CHILD_SIZE = CHILD_SIZE;
    }

    public void initializePopulation() {
        population_coords = new double[POPULATION_SIZE][DIMENSION];
        population_mutation_steps = new double[POPULATION_SIZE][DIMENSION];

        Random rand = new Random();

        // Wspólrzedne inicializowane sa losowo
        for (int i = 0; i < POPULATION_SIZE; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                population_coords[i][j] = rand.nextDouble() * 10;
            }
        }

        for(int i = 0; i < POPULATION_SIZE; i++){
            for(int j = 0; j < DIMENSION; j++){
                population_mutation_steps[i][j] = INITIAL_MUTATION_STEP;
            }
        }

    }

    public void generateChildren () {
        double tau0 = 1.0 / sqrt(2.0 * DIMENSION);
        double tau1 = 1.0 / sqrt(2.0 * sqrt(DIMENSION));

        Random rand = new Random();

        children_coords = new double[CHILD_SIZE][DIMENSION];
        children_mutation_steps = new double[CHILD_SIZE][DIMENSION];

        for(int i = 0; i < CHILD_SIZE; ++i){

            int parentIndex = rand.nextInt(POPULATION_SIZE);

            for(int j = 0; j < DIMENSION; ++j){

                children_coords[i][j] = population_coords[parentIndex][j];
                children_mutation_steps[i][j]  = population_mutation_steps[parentIndex][j];

                if(rand.nextDouble() < MUTATION_RATE){
                    children_mutation_steps[i][j] *= exp(tau0 * rand.nextGaussian() + tau1 * rand.nextGaussian());
                    children_coords[i][j] += rand.nextGaussian() * population_mutation_steps[parentIndex][j];
                }
            }
        }
    }

    public void selectSurvivors() {
        double[] fitnessValues = new double[CHILD_SIZE];

        for(int i = 0; i < CHILD_SIZE; ++i){
            fitnessValues[i] = fitnessFunction(children_coords[i]);
        }
        Arrays.sort(fitnessValues);

        double threshold = fitnessValues[CHILD_SIZE - POPULATION_SIZE];

        population_coords = new double[POPULATION_SIZE][DIMENSION];
        population_mutation_steps = new double[POPULATION_SIZE][DIMENSION];

        int index = 0;
        for(int i = 0; i < CHILD_SIZE; ++i){
            if (fitnessFunction(children_coords[i]) >= threshold){
                if(index < POPULATION_SIZE){
                    population_coords[index] = children_coords[i].clone();
                    population_mutation_steps[index] = children_mutation_steps[i].clone();
                    index++;
                }
                else{
                    break;
                }
            }
        }
    }

    private double fitnessFunction(double[] individual) {
        // Funkcja celu, którą chcesz zminimalizować lub zmaksymalizować
        // Tutaj przykład: funkcja kwadratowa suma (x-5)^2
        double sum = 0;
        for (double value : individual) {
            sum += (value-5) * (value-5);
        }
        return -sum;
    }

    public double[] getBestIndividual() {
        return Arrays.stream(population_coords)
                .max((a, b) -> Double.compare(fitnessFunction(a), fitnessFunction(b)))
                .orElseThrow();
    }
}

