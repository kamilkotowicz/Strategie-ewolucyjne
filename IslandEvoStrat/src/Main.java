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
                MigrantSelectionMode selectionMode, MigrantDeletionMode deletionMode) {
}
public class Main {

    public static void main(String[] args) {

        Settings settings = new Settings(27, 189, 10, 1000,
                IslandModel.RING_ISLANDS, MigrantSelectionMode.RANDOM, MigrantDeletionMode.RANDOM);

        try {
            IslandModels.run(settings);
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    public static double calculate_fitness(double[] coords) {
        // Funkcja celu, którą chcesz zminimalizować lub zmaksymalizować
        // Tutaj przykład: funkcja kwadratowa suma (x-5)^2
        double sum = 0;
        for (double value : coords) {
            sum += (value-5) * (value-5);
        }
        return -sum;
    }
}