import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitOption.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class JarFileFinder {

    public static Path findJarFileInDirectory(Path directory) throws IOException {
        final Path[] foundJar = {null};

        Files.walkFileTree(directory, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().toLowerCase().endsWith(".jar")) {
                    foundJar[0] = file;
                    return FileVisitResult.TERMINATE; // Stop walking the tree once a .jar file is found
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return foundJar[0];
    }

    public static void main(String[] args) {
        try {
            Path directory = Paths.get("C:/JFree"); // Replace with the actual path to your folder
            Path jarFile = findJarFileInDirectory(directory);

            if (jarFile != null) {
                System.out.println("Found .jar file: " + jarFile);
            } else {
                System.out.println("No .jar file found in the specified directory.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
