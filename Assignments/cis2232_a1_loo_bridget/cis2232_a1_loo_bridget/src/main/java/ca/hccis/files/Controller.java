package ca.hccis.files;

import ca.hccis.files.entity.Game;
import ca.hccis.files.entity.Team;
import ca.hccis.files.util.InputUtility;
import com.google.gson.Gson;

import java.util.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Controls the overall flow of the program.
 * @author Bridget Loo
 * @since 2026/09/25
 */
public class Controller {
    public static final String EXIT = "X";
    public static final String MENU = """
            ----- Menu -----
            A) Add Game
            B) View Standings
            C) View Games
            X) Exit
            ----------------""";
    public static final String[] MENU_OPTIONS = {"A", "B", "C", "X"};

    public static final String MESSAGE_ERROR = "Error";
    public static final String MESSAGE_EXIT = "Goodbye";
    public static final String MESSAGE_SUCCESS = "Success";

    // Save data added from the program to a file called:
    public static final String DATA_PATH = "C:\\cis2232\\data_loo_bridget.json";
    public static HashMap<Integer, Game> gameMap = new HashMap<>();
    public static HashMap<String, Team> teamMap = new HashMap<>();
    private static Gson gson = new Gson();

    // TODO DONE Ensure that the directory is created by the program if it does not already exist.
    // TODO DONE Data is saved using JSON.
    // TODO DONE Ensure that newly created entities are saved in the data file.

    static void main(String[] args) {
        initialize();

        String menuOption;

        // TODO DONE Menu is shown until user chooses to exit.
        do {
            menuOption = InputUtility.getInputMenu(MENU, MENU_OPTIONS).toUpperCase();

            switch (menuOption) {
                case EXIT:
                    System.out.println(MESSAGE_EXIT);
                    break;
                case "A":
                    add();
                    break;
                case "B":
                    viewStandings();
                    break;
                case "C":
                    viewGames();
                    break;
                default:
                    System.out.println(MESSAGE_ERROR);
                    break;
            }
        } while (!menuOption.equals(EXIT));
    }

    /**
     * Add a new game entity.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void add() {
        System.out.println("--- Add Game ---");
        Game newGame = new Game();

        if (gameMap.isEmpty()) {
            newGame.getInformation();
        } else {
            int gameID = InputUtility.getInputInt("Game Number: ", 0, -1, false);

            if (!gameMap.containsKey(gameID)) {
                newGame.getInformation(gameID);
            } else {
                // TODO Display existing record.
                boolean overwriteGame = InputUtility.getInputBoolean("This game number is already tracked. Would you like to overwrite the existing record?");

                if (overwriteGame) {
                    newGame.getInformation(gameID);
                } else return; // Exit early if the user doesn't want to overwrite the game.
            }
        }

        gameMap.put(newGame.getGameId(), newGame);
        recordTeams(newGame); // Update the teamMap for the two teams in the game.
        writeAll();
    }

    /**
     * View standings for each team referenced in all tracked games.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void viewStandings() {
        // TODO DONE Display team info sorted by number of wins.
        teamMap.values().stream().sorted(Comparator.comparing(Team::getWins)).forEach(Team::display);
    }

    public static void viewGames() {
        // TODO DONE Display games in order of id.
        gameMap.values().stream().sorted(Comparator.comparing(Game::getGameId)).forEach(Game::display);
    }

    /**
     * Initialize the program so that any existing data is pulled in.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void initialize() {
        Path path = Paths.get(DATA_PATH);

        try {
            // Make sure the target file path has a parent directory and if it doesn't exist, create it.
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            // Check if the file exists already. If it does, get data from it.
            if (Files.exists(path)) {
                readAll();
            }

            // If the file does not exist yet, it will be created the first time a write command is called.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the teams from a single game to the teamMap HashMap or update an existing team record.
     * @param game The Game object with the teams to add.
     * @author Bridget Loo
     * @since 20260925
     */
    private static void recordTeams(Game game) {
        for (String teamName : game.getTeamNames()) {
            Team team = teamMap.computeIfAbsent(teamName, Team::new);

            if (teamName.equals(game.getWinningTeam())) {
                team.addWin();
                if (game.isMercyWin()) team.addMercyWin();
            } else team.addLoss();
        }
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
                Game gameFromJson = gson.fromJson(line, Game.class);
                gameMap.put(gameFromJson.getGameId(), gameFromJson);
                recordTeams(gameFromJson);
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
        try {
            // false means the file will be overwritten instead of new content appended to the end.
            FileWriter writer = new FileWriter(DATA_PATH, false);
            int gamesWritten = 0;

            for (Game game : gameMap.values()) {
                writer.append(gson.toJson(game));
                writer.append(System.lineSeparator());
                gamesWritten++;
            }

            if (gamesWritten == gameMap.size()) {
                System.out.println(MESSAGE_SUCCESS);
            } else {
                System.out.println(MESSAGE_ERROR + ": " + gamesWritten + " of " + gameMap.size() + " games recorded were written to file.");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
