package ca.hccis.files.threads;

import ca.hccis.files.entity.Game;
import ca.hccis.files.entity.Team;
import ca.hccis.files.util.GameUtility;

import java.io.*;
import java.util.*;

import static ca.hccis.files.Controller.*;

/**
 * Thread to run.
 * @author BML
 * @since 20261010
 */
public class ThreadGame extends Thread {
    public static final String EXIT = "X";
    public static final String MENU = """
            ----- Menu -----
            A) Add Game
            B) View Standings
            X) Exit
            ----------------""";

    public static final String MESSAGE_ERROR = "Error";
    public static final String MESSAGE_EXIT = "Goodbye";
    public static final String MESSAGE_SUCCESS = "Success";

    private GameUtility gameUtility = null;

    // TODO A1 DONE: Data is saved using JSON.
    // TODO A1 DONE: Ensure that newly created entities are saved in the data file.

    public ThreadGame() {}

    public ThreadGame(GameUtility gameUtility) {
        this.gameUtility = gameUtility;
    }

    /**
     * Run the thread.
     * @author Bridget Loo
     * @since 20260925
     */
    public void run() {
        String menuOption;

        // TODO A1 DONE: Menu is shown until user chooses to exit.
        do {
            // If JOptionPane is being used and user clicks Cancel, it acts like an Exit selection.
            menuOption = gameUtility.getInputString(MENU, true).trim().toUpperCase();

            switch (menuOption) {
                case EXIT:
                    gameUtility.display(MESSAGE_EXIT);
                    break;
                case "A":
                    add();
                    break;
                case "B":
                    viewStandings();
                    break;
                default:
                    gameUtility.display(MESSAGE_ERROR);
                    break;
            }
        } while (!menuOption.equals(EXIT));
    }

    /**
     * Add a new game entity.
     * @author Bridget Loo
     * @since 20260925
     * @modified 20261010 BML No longer static and lines moved around/modified to support locking/synchronization.
     */
    public void add() {
        Game newGame = new Game(gameUtility);

        // TODO: Handle game still being added when user cancels on setting Home or Road team names.
        // Get the team names first and check whether the game has been tracked already or not.
        newGame.setTeamNames();

        boolean gameExists = false;

        // Lock so that this thread can check the maps without interference from another thread.
        synchronized (LOCK) {
            if (!gameMap.isEmpty()) {
                for (Game game : gameMap.values()) {
                    if (game.equals(newGame)) {
                        gameExists = true;
                        // Keep track of the existing game's ID.
                        newGame.setGameId(game.getGameId());
                        break;
                    }
                }
            }
        }

        Optional<Boolean> overwriteGame = Optional.of(false);

        if (gameExists) overwriteGame = gameUtility.getInputBoolean("This game is already tracked. Would you like to overwrite the existing record?");

        // Exit early if: overwriteGame is empty (the user canceled) OR the game exists, overwriteGame is present and is false.
        if (overwriteGame.isEmpty() || (gameExists && !overwriteGame.get())) return;
        else newGame.getGameInfo(); // Otherwise, get the game info from the user.

        int totalGames; // The total number of games that have been tracked.

        // Lock so that this thread can modify the maps and data file without interference from another thread.
        synchronized (LOCK) {
            // Confirm again that the game doesn't already exist (i.e. hasn't been added by another thread since the initial test. If it already exists, notify the user that something went wrong and exit early).
            if (gameMap.containsValue(newGame)) {
                gameUtility.display("Something went wrong while attempting to track the game. Make sure it hasn't already been tracked.");
                return;
            }

            if (overwriteGame.get()) {
                // Remove the existing game from the map.
                gameMap.remove(newGame.getGameId());

                // TODO DONE: Remove the game from the teamMap too.
                teamMap.get(newGame.getHomeTeam()).removeGame(newGame);
                teamMap.get(newGame.getRoadTeam()).removeGame(newGame);
            } else {
                // Give the new game a new ID.
                newGame.setGameId(gameMap.isEmpty() ? 1 : Collections.max(gameMap.keySet()) + 1);
            }

            gameMap.put(newGame.getGameId(), newGame);
            recordTeams(newGame); // Update the teamMap for the two teams in the game.
            if (overwriteGame.get()) writeAll(); // Rewrite the file so that the old game data is removed.
            else writeGame(newGame);
            totalGames = gameMap.size();
        }

        // @modified 20261010 BML Display accumulator count.
        // TODO A3 DONE: Ensure that after each entity is created an accumulator variable is updated and displayed.
        gameUtility.display("Total games tracked (both threads): " + totalGames);
    }

    /**
     * Add the teams from a single game to the teamMap HashMap or update an existing team record.
     * @param game The Game object with the teams to add.
     * @author Bridget Loo
     * @since 20260925
     * @modified 20261010 BML Made public.
     */
    public static void recordTeams(Game game) {
        for (String teamName : game.getTeamNames()) {
            Team team = teamMap.computeIfAbsent(teamName, Team::new);
            team.addGame(game);
        }
    }

    /**
     * Append a new game to the end of the data file. Replaces the previous writeAll() method.
     * @param game The game to write to file.
     * @author Bridget Loo
     * @since 20261010
     */
    public static void writeGame(Game game) {
        // TODO A3 DONE: Close the writer and open a new one each time so that the file doesn't stay locked.
        try (FileWriter dataWriter = new FileWriter(dataPath.toString(), true)) {
            dataWriter.write(game.toJson() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the whole gameMap to file.
     * @author Bridget Loo
     * @since 20261010
     */
    public static void writeAll() {
        try (FileWriter dataWriter = new FileWriter(dataPath.toString(), false)) {
            for (Game game : gameMap.values()) {
                dataWriter.write(game.toJson() + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * View standings for each team referenced in all tracked games.
     * @author Bridget Loo
     * @since 20260925
     * @modified 20261010 BML Updated display implementation and no longer static.
     */
    public void viewStandings() {
        // TODO: If multiple teams have the same number of wins, fallback on the win percentage for sort.
        List<Team> teams;

        synchronized (LOCK) {
            teams = teamMap.values().stream()
                    // TODO DONE: Display team info sorted by number of wins.
                    .sorted(Comparator.comparing(Team::getWins).reversed())
                    .toList();
        }

        // TODO A3 DONE: Build one string to show results so that there aren't lots of popups if the display is GUI.
        StringBuilder output = new StringBuilder();
        if (teams.isEmpty()) output.append("No teams found. Try tracking some games first.");
        else {
            output.append("--- Team Standings ---").append(System.lineSeparator());

            for (Team team : teams) {
                output.append(team.toString()).append(System.lineSeparator());
                if (gameUtility.isGUI) output.append(System.lineSeparator()); // Add another empty line between teams if the display is GUI.
            }
        }

        gameUtility.display(output.toString());
    }
}
