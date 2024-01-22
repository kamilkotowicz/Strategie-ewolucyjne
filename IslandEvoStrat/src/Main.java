enum IslandModel{
    WITHOUT_ISLANDS,
    RING_ISLANDS,
    TORUS_ISLANDS
}

enum MigrantSelectionMode{
    BEST_FITNESS,
    RANDOM
}

enum MigrantDeletionMode{
    WORST_FITNESS,
    RANDOM
}

record Settings(int POPULATION_SIZE, int NUM_OF_CHILDREN, int DIMENSIONS, int ITERATIONS, IslandModel model,
                MigrantSelectionMode selectionMode, MigrantDeletionMode deletionMode, boolean displaySolution) {
}
public class Main {

    public static void main(String[] args) {

        Settings settings = new Settings(270, 1890, 10, 1000,
                IslandModel.TORUS_ISLANDS, MigrantSelectionMode.BEST_FITNESS, MigrantDeletionMode.WORST_FITNESS, false);

        try {
            IslandModels.test(settings);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public static double calculate_fitness(double[] coords) {
        TestFunction testFunction = new SquareFunction();
        return testFunction.calculateFitness(coords);
    }
}