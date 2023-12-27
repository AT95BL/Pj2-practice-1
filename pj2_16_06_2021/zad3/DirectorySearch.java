import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectorySearch {
    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 3) {
            System.out.println("Usage: java DirectorySearch <source_directory> <destination_directory> <file_extension>");
            System.exit(1);
        }

        // Parse command line arguments
        String sourceDirectoryPath = args[0];
        String destinationDirectoryPath = args[1];
        String fileExtension = args[2];

        // Perform directory search and file copy
        try {
            // Create Path objects for source and destination directories
            Path sourceDirectory = Paths.get(sourceDirectoryPath);
            Path destinationDirectory = Paths.get(destinationDirectoryPath);

            // Create destination directory if it doesn't exist
            Files.createDirectories(destinationDirectory);

            // Search for files with the given extension
            Map<Path, Integer> filesWithExtension = searchFilesWithExtension(sourceDirectory, fileExtension);

            // Copy matching files to the destination directory
            copyFilesToDestination(filesWithExtension.keySet(), destinationDirectory);

            // Display results
            displayResults(filesWithExtension);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Path, Integer> searchFilesWithExtension(Path directory, String fileExtension) throws IOException {
        Map<Path, Integer> result = new HashMap<>();
    
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // Check if the file has the specified extension
                if (file.toString().toLowerCase().endsWith("." + fileExtension.toLowerCase())) {
                    result.put(file, result.containsKey(file) ? result.get(file) + 1 : 1);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    
        return result;
    }
    

    private static void copyFilesToDestination(Iterable<Path> files, Path destinationDirectory) throws IOException {
        for (Path file : files) {
            Files.copy(file, destinationDirectory.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void displayResults(Map<Path, Integer> filesWithExtension) {
        System.out.println("Results:");
        for (Map.Entry<Path, Integer> entry : filesWithExtension.entrySet()) {
            System.out.println("File: " + entry.getKey() + " | Occurrences: " + entry.getValue());
        }
    }
}
