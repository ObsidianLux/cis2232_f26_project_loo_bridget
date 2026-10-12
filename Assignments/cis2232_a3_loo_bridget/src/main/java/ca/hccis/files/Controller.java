package ca.hccis.files;

import ca.hccis.files.entity.Game;
import ca.hccis.files.entity.Team;
import ca.hccis.files.threads.ThreadGame;
import ca.hccis.files.util.GameUtility;
import com.google.gson.Gson;

import java.io.File;
import java.util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

import static ca.hccis.files.threads.ThreadGame.recordTeams;

/**
 * Controls the overall flow of the program.
 * @author Bridget Loo
 * @since 2026/09/25
 * @modified 20261010 BML Moved almost everything into a new ThreadGame class and reworked the controller to use threads.
 */
public class Controller {
    // @modified 20261010 BML Accumulate games and teams from all threads. Changed to ConcurrentHashMap for synchronization.
    public static ConcurrentHashMap<Integer, Game> gameMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Team> teamMap = new ConcurrentHashMap<>();

    public static final Path dataPath = Paths.get("C:\\cis2232\\data_loo_bridget.json");

    // This object will be used by threads when they make changes to the data file to lock it and prevent another thread from slipping in between lines and messing things up.
    public static final Object LOCK = new Object();

    public static void main(String[] args) {
        initialize();

        Thread threadConsole = new ThreadGame(new GameUtility(false));
        Thread threadGUI = new ThreadGame(new GameUtility(true));

        threadConsole.start();
        threadGUI.start();
    }

    /**
     * Initialize the program so that any existing data is pulled in.
     */
    private static void initialize() {
        try {
            // TODO A1 DONE: Ensure that the directory is created by the program if it does not already exist.
            if (dataPath.getParent() != null) Files.createDirectories(dataPath.getParent());

            // @modified 20261010 BML If the file doesn't exist already, create a new one. Otherwise, read the file and store game data.
            if (!new File(dataPath.toString()).createNewFile()) readAll();
        } catch (IOException e) {
            // TODO: Should I throw a RuntimeException(e) instead?
            e.printStackTrace();
        }
    }

    /**
     * Read the contents of the data file (if it exists) and store it in a HashMap.
     * @author Bridget Loo
     * @since 20260925
     */
    public static void readAll() {
        Gson gson = new Gson();

        try {
            List<String> lines = Files.readAllLines(dataPath);

            if (lines.isEmpty()) return;

            for (String line : lines) {
                Game gameFromJson = gson.fromJson(line, Game.class);
                gameMap.put(gameFromJson.getGameId(), gameFromJson);
                recordTeams(gameFromJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
