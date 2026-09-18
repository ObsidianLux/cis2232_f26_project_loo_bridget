package ca.hccis.files;

import ca.hccis.files.entity.Team;
import ca.hccis.files.util.InputUtility;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controls the overall flow of the program.
 * @author Bridget Loo
 * @since 2026/09/25
 */
public class Controller {
    public static final String EXIT = "X";
    public static final String MENU = """
            ----- Menu -----
            A) Add
            B) View
            X) Exit
            ----------------""";
    public static final String[] MENU_OPTIONS = {"A", "B", "X"};

    public static final String MESSAGE_ERROR = "Error";
    public static final String MESSAGE_EXIT = "Goodbye";
    public static final String MESSAGE_SUCCESS = "Success";

    // Save data added from the program to a file called:
    public static final String DATA_PATH = "c:\\cis2232\\data_loo_bridget.json";
    public static HashMap<Integer, Team> teamMap = new HashMap<>();
    private static Gson gson = new Gson();

    // TODO Ensure that the directory is created by the program if it does not already exist.
    // TODO Data is saved using JSON.
    // TODO Ensure that newly created entities are saved in the data file.

    static void main(String[] args) {
        initialize();

        String menuOption;

        // TODO DONE Menu is shown until user chooses to exit.
        do {
            menuOption = InputUtility.getInputMenu(MENU, MENU_OPTIONS);

            switch (menuOption) {
                case EXIT:
                    System.out.println(MESSAGE_EXIT);
                    break;
                case "A":
                    add();
                    break;
                case "B":
                    viewAll();
                    break;
                default:
                    System.out.println(MESSAGE_ERROR);
                    break;
            }
        } while (!menuOption.equals(EXIT));
    }

    /**
     * Add a new entity.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void add() {

    }

    /**
     * View all existing entities.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void viewAll() {

    }

    /**
     * Initialize the program so that any existing data is pulled in.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void initialize() {
        Path path = Paths.get(DATA_PATH);

        // Check if the file exists already.
        if (Files.exists(path)) {
            System.out.println("Managed Teams:");
            readAll();
        } else System.out.println("No Managed Teams.");
    }

    /**
     * Read the contents of the data file (if it exists) and store it in a HashMap.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void readAll() {
        try {
            FileReader reader = new FileReader(DATA_PATH);
            List<String> lines = reader.readAllLines();
            for (String line : lines) {
                Team teamFromJson = gson.fromJson(line, Team.class);
                teamMap.put(teamFromJson.getId(), teamFromJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the contents of the HashMap to the data file.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void writeAll() {

    }
}
