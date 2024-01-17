import java.util.Arrays;
public class Main {
    public static void main(String[] args) {

        final int POPULATION_SIZE = 30;
        final int DIMENSION = 10;
        final int ITERATIONS = 1000;
        final int CHILD_SIZE = 200;

        EvolutionaryAlgorithm algo = new EvolutionaryAlgorithm(POPULATION_SIZE, DIMENSION, ITERATIONS, CHILD_SIZE);
        algo.initializePopulation();

        for (int iteration = 0; iteration < ITERATIONS; iteration++) {
            algo.generateChildren();
            algo.selectSurvivors();
        }

        double[] bestIndividual = algo.getBestIndividual();
        System.out.println("Best individual: " + Arrays.toString(bestIndividual));
    }
}