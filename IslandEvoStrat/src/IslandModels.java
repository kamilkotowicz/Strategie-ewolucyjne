import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandModels {

    private static Individual getBestIndividual(Individual bestIndividual, Population island){
        Individual bestIndividualOnIsland = island.getIndividualWithLargestFitness();
        double bestFitnessOnIsland = bestIndividualOnIsland.fitness;
        double bestFitness = bestIndividual.fitness;

        if(bestFitnessOnIsland > bestFitness){
            bestIndividual = bestIndividualOnIsland;
        }
        return bestIndividual;
    }

    public static double[] withoutIslands(Settings settings){
        final int DIMENSIONS = settings.DIMENSIONS();
        final int ITERATIONS = settings.ITERATIONS();
        final int POPULATION_SIZE = settings.POPULATION_SIZE();
        final int NUM_OF_CHILDREN = settings.NUM_OF_CHILDREN();

        Population population = new Population();
        population.initializePopulation(POPULATION_SIZE, DIMENSIONS);

        double[] bestFitnesses = new double[ITERATIONS];

        for(int iteration = 0; iteration < ITERATIONS; ++iteration){
            population.nextGeneration(NUM_OF_CHILDREN, DIMENSIONS);
            population.keepSurvivors(POPULATION_SIZE);

            Individual bestIndividualInIteration = population.getIndividualWithLargestFitness();
            double bestFitnessInIteration = bestIndividualInIteration.fitness;
            bestFitnesses[iteration] = bestFitnessInIteration;
        }

        Individual best_individual = population.getIndividualWithLargestFitness();
        double[] bestCoords = best_individual.coords;

        if(settings.displaySolution()){
            System.out.println("Best solution: " + Arrays.toString(bestCoords));
        }

        return bestFitnesses;

    }
    public static double[] ringIslands(Settings settings){
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

        double[] bestFitnesses = new double[ITERATIONS];

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

            Individual bestIndividualInIteration = new Individual();

            for(int i = 0; i < NUM_OF_ISLANDS; ++i){
                bestIndividualInIteration = getBestIndividual(bestIndividualInIteration, islands[i]);
            }

            double bestFitnessInIteration = bestIndividualInIteration.fitness;
            bestFitnesses[iteration] = bestFitnessInIteration;
        }

        Individual bestIndividual = new Individual();

        for(int i = 0; i < NUM_OF_ISLANDS; ++i){
            bestIndividual = getBestIndividual(bestIndividual, islands[i]);
        }

        double[] bestCoords = bestIndividual.coords;

        if(settings.displaySolution()){
            System.out.println("Best solution: " + Arrays.toString(bestCoords));
        }

        return bestFitnesses;

    }

    public static double[] torusIslands(Settings settings){
        final int MIGRATION_INTERVAL = 10;
        final int NUM_OF_ISLANDS_PER_ROW = 3;
        final int NUM_OF_MIGRANTS = 4;
        final int DIMENSIONS = settings.DIMENSIONS();
        final int ITERATIONS = settings.ITERATIONS();
        int POPULATION_SIZE_PER_ISLAND = settings.POPULATION_SIZE() / (NUM_OF_ISLANDS_PER_ROW * NUM_OF_ISLANDS_PER_ROW);
        int NUM_OF_CHILDREN_PER_ISLAND = settings.NUM_OF_CHILDREN() / (NUM_OF_ISLANDS_PER_ROW * NUM_OF_ISLANDS_PER_ROW);
        MigrantSelectionMode selectionMode = settings.selectionMode();
        MigrantDeletionMode deletionMode = settings.deletionMode();

        Population[][] islands = new Population[NUM_OF_ISLANDS_PER_ROW][NUM_OF_ISLANDS_PER_ROW];

        for(int i = 0; i < NUM_OF_ISLANDS_PER_ROW; ++i){
            for(int j = 0; j < NUM_OF_ISLANDS_PER_ROW; ++j){
                islands[i][j] = new Population();
                islands[i][j].initializePopulation(POPULATION_SIZE_PER_ISLAND, DIMENSIONS);
            }
        }

        double[] bestFitnesses = new double[ITERATIONS];

        for(int iteration = 0; iteration < ITERATIONS; ++iteration){
            for(int i = 0; i < NUM_OF_ISLANDS_PER_ROW; ++i){
                for(int j = 0; j < NUM_OF_ISLANDS_PER_ROW; ++j){
                    islands[i][j].nextGeneration(NUM_OF_CHILDREN_PER_ISLAND, DIMENSIONS);
                    islands[i][j].keepSurvivors(POPULATION_SIZE_PER_ISLAND);
                }
            }

            if(iteration % MIGRATION_INTERVAL == 0 && iteration > 0){
                List<Individual>[][] k_best_per_island = new ArrayList[NUM_OF_ISLANDS_PER_ROW][NUM_OF_ISLANDS_PER_ROW];
                List<Individual>[][] k_worst_per_island = new ArrayList[NUM_OF_ISLANDS_PER_ROW][NUM_OF_ISLANDS_PER_ROW];

                for(int i = 0; i < NUM_OF_ISLANDS_PER_ROW; ++i){
                    for(int j = 0; j < NUM_OF_ISLANDS_PER_ROW; ++j){
                        k_best_per_island[i][j] = (selectionMode == MigrantSelectionMode.BEST_FITNESS) ?
                                islands[i][j].getKIndividualsWithLargestFitness(NUM_OF_MIGRANTS) : islands[i][j].getKRandomIndividuals(NUM_OF_MIGRANTS);

                        for (Individual individual : k_best_per_island[i][j]){
                            islands[i][j].removeIndividual(individual);
                        }

                        // Usuwane jest 3 razy wiecej najgorszych niz najlepszych, aby rozmiar populacji sie nie zmienil
                        // poniewaz w torusie kazda wyspa ma 4 sasiadow
                        k_worst_per_island[i][j] = (deletionMode == MigrantDeletionMode.WORST_FITNESS) ?
                                islands[i][j].getKIndividualsWithSmallestFitness(NUM_OF_MIGRANTS * 3) : islands[i][j].getKRandomIndividuals(NUM_OF_MIGRANTS * 3);

                        for(Individual individual : k_worst_per_island[i][j]){
                            islands[i][j].removeIndividual(individual);
                        }

                    }
                }

                for(int i = 0; i < NUM_OF_ISLANDS_PER_ROW; ++i){

                    int left_island = i > 0 ? i - 1 : NUM_OF_ISLANDS_PER_ROW - 1;
                    int right_island = i < NUM_OF_ISLANDS_PER_ROW - 1 ? i + 1 : 0;

                    for(int j = 0; j < NUM_OF_ISLANDS_PER_ROW; ++j){
                        int up_island = j > 0 ? j - 1 : NUM_OF_ISLANDS_PER_ROW - 1;
                        int down_island = j < NUM_OF_ISLANDS_PER_ROW - 1 ? j + 1 : 0;

                        for(Individual individual : k_best_per_island[left_island][j]){
                            islands[i][j].addIndividual(individual);
                        }

                        for(Individual individual : k_best_per_island[right_island][j]){
                            islands[i][j].addIndividual(individual);
                        }

                        for(Individual individual : k_best_per_island[i][up_island]){
                            islands[i][j].addIndividual(individual);
                        }

                        for(Individual individual : k_best_per_island[i][down_island]){
                            islands[i][j].addIndividual(individual);
                        }
                    }
                }

            }

            Individual bestIndividualInIteration = new Individual();

            for(int i = 0; i < NUM_OF_ISLANDS_PER_ROW; ++i){
                for(int j = 0; j < NUM_OF_ISLANDS_PER_ROW; ++j){
                    bestIndividualInIteration = getBestIndividual(bestIndividualInIteration, islands[i][j]);
                }
            }

            double bestFitnessInIteration = bestIndividualInIteration.fitness;
            bestFitnesses[iteration] = bestFitnessInIteration;
        }

        Individual bestIndividual = new Individual();

        for(int i = 0; i < NUM_OF_ISLANDS_PER_ROW; ++i){
            for(int j = 0; j < NUM_OF_ISLANDS_PER_ROW; ++j){
                bestIndividual = getBestIndividual(bestIndividual, islands[i][j]);
            }
        }

        double[] bestCoords = bestIndividual.coords;

        if(settings.displaySolution()){
            System.out.println("Best solution: " + Arrays.toString(bestCoords));
        }

        return bestFitnesses;
    }

    public static double[] run(Settings settings) throws Exception {
        IslandModel model = settings.model();

        switch (model) {
            case WITHOUT_ISLANDS -> {
                return withoutIslands(settings);
            }
            case RING_ISLANDS -> {
                return ringIslands(settings);
            }
            case TORUS_ISLANDS -> {
                return torusIslands(settings);
            }
            default -> throw new Exception("Island model doesn't exists");
        }
    }

    public static void test(Settings settings) throws Exception {
        final int NUM_OF_INSTANCES = 10;

        double [][] bestFitnesses = new double[NUM_OF_INSTANCES][settings.ITERATIONS()];

        for(int i = 0; i < NUM_OF_INSTANCES; ++i){
            bestFitnesses[i] = run(settings);
        }

        double[] meanBestFitness = new double[settings.ITERATIONS()];

        for(int i = 0; i < settings.ITERATIONS(); ++i){
            for(int j = 0; j < NUM_OF_INSTANCES; ++j){
                meanBestFitness[i] += bestFitnesses[j][i] / NUM_OF_INSTANCES;
            }
        }

        System.out.println("Mean best fitness: " + Arrays.toString(meanBestFitness));

    }
}
