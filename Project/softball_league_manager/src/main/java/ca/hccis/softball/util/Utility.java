package ca.hccis.softball.util;

import ca.hccis.softball.jpa.entity.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class containing utility methods that may be used throughout the project.
 * @author BML
 * @since 20261025
 */
public class Utility {
    public static final String DEFAULT_DIRECTORY = "c:\\CIS2232\\";
    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    /**
     * Get today's date in the "yyyy-MM-dd" format.
     * @return Today's date
     * @author BML
     * @since 20261025
     */
    public static String getCurrentDate() {
        // Uses the default date format of "yyyy-MM-dd"
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Get today's date in the provided format. Defaults to "yyyy-MM-dd" if the given format is invalid.
     * @param format The format to use
     * @return Today's date
     * @author BML
     * @since 20261025
     */
    public static String getCurrentDate(String format) {
        // Use the default date format if the provided format is invalid.
        if (format == null || format.isEmpty()) return getCurrentDate();
        else return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * Write a list of Game objects to a file.
     * @param fileName The first part of the file name. The current date and the ".txt" extension will be appended to the name.
     * @param games A list of Games to write to file.
     * @throws IOException Throws if there are issues with creating or accessing the directory or file or with the file writer.
     * @author BML
     * @since 20261025
     */
    public static void writeGamesToFile(String fileName, ArrayList<Game> games) throws IOException {
        ArrayList<String> lines = games.stream()
                .map(Game::toString)
                .collect(Collectors.toCollection(ArrayList::new));

        writeReportToFile(fileName, lines);
    }

    /**
     * Write a list of lines to a file.
     * @param fileName The first part of the file name. The current date and the ".txt" extension will be appended to the name.
     * @param lines A list of Strings to write to file.
     * @throws IOException Throws if there are issues with creating or accessing the directory or file or with the file writer.
     * @author BML
     * @since 20261025
     */
    private static void writeReportToFile(String fileName, ArrayList<String> lines) throws IOException {
        // Append the current date to the given filename along with the .txt file extension.
        String fullFileName = DEFAULT_DIRECTORY + fileName + getCurrentDate("_yyyyMMddhhmmss") + ".txt";

        // Get the path object for the directory.
        Path path = Paths.get(DEFAULT_DIRECTORY);

        // Attempt to create the directory. If it already exists, nothing happens.
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            logger.error("Utility.java - Error creating directories.");
        }

        // Write to the file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFileName, true))) {
            try {

                for (String current : lines) {
                    writer.write(current);
                    writer.newLine();
                }
            } catch (IOException e) {
                logger.error("Utility.java - Exception writing to file.");
            }
        } catch (IOException e) {
            logger.error("Utility.java - Exception with buffered file writer.");
        }
    }
}
