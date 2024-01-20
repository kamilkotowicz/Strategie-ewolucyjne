import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandModels {
    public static void withoutIslands(Settings settings){
        final int DIMENSIONS = settings.DIMENSIONS();
        final int ITERATIONS = settings.ITERATIONS();
        final int POPULATION_SIZE = settings.POPULATION_SIZE();
        final int NUM_OF_CHILDREN = settings.NUM_OF_CHILDREN();

        Population population = new Population();
        population.initializePopulation(POPULATION_SIZE, DIMENSIONS);

        for(int iteration = 0; iteration < ITERATIONS; ++iteration){
            population.nextGeneration(NUM_OF_CHILDREN, DIMENSIONS);
            population.keepSurvivors(POPULATION_SIZE);
        }

        Individual best_individual = population.getIndividualWithLargestFitness();
        double[] bestCoords = best_individual.coords;
        System.out.println("Best solution: " + Arrays.toString(bestCoords));

    }
    public static void ringIslands(Settings settings){
        final int MIGRATION_INTERVAL = 10;
        final int NUM_OF_ISLANDS = 9;
        final int NUM_OF_MIGRANTS = 4;
        final int DIMENSIONS = settings.DIMENSIONS();
        final int ITERATIONS = settings.ITERATIONS();
        int POPULATION_SIZE_PER_ISLAND = settings.POPULATION_SIZE() / NUM_OF_ISLANDS;
        int NUM_OF_CHILDREN_PER_ISLAND = settings.NUM_OF_CHILDREN() / NUM_OF_ISLANDS;
        MigrantSelectionMode selectionMode = settings.selectionMode();
        MigrantDeletionMode deletionMode = settings.deletionMode();

        Population[] islands = new Population[NUM_OF_ISLANDS];
        for(int i = 0; i < NUM_OF_ISLANDS; ++i){
            islands[i] = new Population();
            islands[i].initializePopulation(POPULATION_SIZE_PER_ISLAND, DIMENSIONS);
        }

        for(int iteration = 0; iteration < ITERATIONS; ++iteration){
            for(int i = 0; i < NUM_OF_ISLANDS; ++i){
                islands[i].nextGeneration(NUM_OF_CHILDREN_PER_ISLAND, DIMENSIONS);
                islands[i].keepSurvivors(POPULATION_SIZE_PER_ISLAND);
            }
            if(iteration % MIGRATION_INTERVAL == 0 && iteration > 0){

                List<Individual>[] k_best_per_island = new ArrayList[NUM_OF_ISLANDS];
                List<Individual>[] k_worst_per_island = new ArrayList[NUM_OF_ISLANDS];

                for(int i = 0; i < NUM_OF_ISLANDS; ++i){
                    k_best_per_island[i] = (selectionMode == MigrantSelectionMode.BEST_FITNESS) ?
                            islands[i].getKIndividualsWithLargestFitness(NUM_OF_MIGRANTS) : islands[i].getKRandomIndividuals(NUM_OF_MIGRANTS);

                    // Kolejnosc instrukcji jest wazna!
                    // Najpierw usuwamy k najlepszych osobnikow, aby na pewno ich nie wylosowac ponownie jako najslabszych

                    for (Individual individual : k_best_per_island[i]){
                        islands[i].removeIndividual(individual);
                    }

                    k_worst_per_island[i] = (deletionMode == MigrantDeletionMode.WORST_FITNESS) ?
                            islands[i].getKIndividualsWithSmallestFitness(NUM_OF_MIGRANTS) : islands[i].getKRandomIndividuals(NUM_OF_MIGRANTS);

                    for(Individual individual : k_worst_per_island[i]){
                        islands[i].removeIndividual(individual);
                    }
                }

                for(int i = 0; i < NUM_OF_ISLANDS; ++i){
                    int left_island = i > 0 ? i - 1 : NUM_OF_ISLANDS - 1;
                    int right_island = i < NUM_OF_ISLANDS - 1 ? i + 1 : 0;

                    for(Individual individual : k_best_per_island[left_island]){
                        islands[i].addIndividual(individual);
                    }

                    for(Individual individual : k_best_per_island[right_island]){
                        islands[i].addIndividual(individual);
                    }
                }

            }
        }



        Individual bestIndividual = new Individual();
        double bestFitness = Double.NEGATIVE_INFINITY;

        for(int i = 0; i < NUM_OF_ISLANDS; ++i){
            Individual bestIndividualOnIsland = islands[i].getIndividualWithLargestFitness();
            double bestFitnessOnIsland = bestIndividualOnIsland.fitness;

            if(bestFitnessOnIsland > bestFitness){
                bestFitness = bestFitnessOnIsland;
                bestIndividual = bestIndividualOnIsland;
            }
        }

        double[] bestCoords = bestIndividual.coords;
        System.out.println("Best solution: " + Arrays.toString(bestCoords));

    }

    public static void torusIslands(Settings settings){
        System.out.println("Not implemented yet");
    }

    public static void run(Settings settings) throws Exception {
        IslandModel model = settings.model();

        switch (model) {
            case WITHOUT_ISLANDS -> withoutIslands(settings);
            case RING_ISLANDS -> ringIslands(settings);
            case TORUS_ISLANDS -> torusIslands(settings);
            default -> throw new Exception("Island model doesn't exists");
        }
    }
}