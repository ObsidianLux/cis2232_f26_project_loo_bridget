package ca.hccis.softball.model.bo;

import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.model.domain.Team;
import ca.hccis.softball.model.entity.Game;

import java.util.*;

public class GameBO {
    /**
     * Get the name of the winning team.
     * @param game The game to check
     * @return The name of the winning team
     * @author BML
     * @since 20261025
     */
    public static String getWinningTeam(Game game) {
        return game.getHomeScore() > game.getRoadScore() ? game.getHomeTeam() : game.getRoadTeam();
    }

    /**
     * Get the score differential for a specific team in a game.
     * @param game The game to get the differential from
     * @param teamName The team to get the differential for
     * @return The score differential
     * @author BML
     * @since 20261103
     */
    public static String getPlusMinus(Game game, String teamName) {
        boolean isWinner = teamName.equals(getWinningTeam(game));
        int scoreDifference = Math.abs(game.getHomeScore() - game.getRoadScore());
        return (isWinner ? "+" : "-") + scoreDifference;
    }

    /**
     * Checks which team has the higher score and returns the name of the other one.
     * @param game The game to check for
     * @return The name of the team that forfeited
     * @author BML
     * @since 20261107
     */
    public static String getTeamAtFault(Game game) {
        return game.getHomeScore() > game.getRoadScore() ? game.getRoadTeam() : game.getHomeTeam();
    }

    /**
     * Update an existing game to indicate that it was forfeited.
     * @param game The game to update
     * @param teamAtFault The team who forfeited
     * @return The updated game
     * @author BML
     * @since 20261107
     */
    public static Game setPlayForfeit(Game game, String teamAtFault) {
        game.setPlayed(true);
        game.setWasForfeited(true);
        game.setWasMercyWin(false);

        if (game.getHomeTeam().equals(teamAtFault)) {
            game.setHomeScore(0);
            game.setRoadScore(7);
        } else {
            game.setHomeScore(7);
            game.setRoadScore(0);
        }

        return game;
    }

    /**
     * Update an existing game to indicate that it was played.
     * @param game The game to update
     * @return The updated game
     * @author BML
     * @since 20261107
     */
    public static Game setPlayNotForfeit(Game game) {
        game.setPlayed(true);
        game.setWasForfeited(false);

        int homeScore = 0;
        int roadScore = 0;

        for (Inning inning : game.getInnings()) {
            homeScore += inning.getHomeRuns();
            roadScore += inning.getRoadRuns();
        }

        game.setHomeScore(homeScore);
        game.setRoadScore(roadScore);

        game.setWasMercyWin(game.getInnings().size() == 5);

        return game;
    }

    /**
     * Get a list of all unique teams in the league sorted by their win percentage.
     * @return List of teams
     * @author BML
     * @since 20261020
     */
    public static ArrayList<Team> selectAllTeams() {
        // LinkedHashMap keeps the teams in the order they first appear while making sure each team is only created once.
        Map<String, Team> teams = new LinkedHashMap<>();

        for (Game game : new GameDAO().selectAll()) {
            teams.computeIfAbsent(game.getHomeTeam(), Team::new).addGame(game);
            teams.computeIfAbsent(game.getRoadTeam(), Team::new).addGame(game);
        }

        // Returns an empty list if no teams were found.
        return new ArrayList<>(teams.values());
    }

    /**
     * Get a list of all unique teams in the league sorted by their win percentage.
     * @return List of teams
     * @author BML
     * @since 20261111
     */
    public static ArrayList<Team> selectAllTeamsSortedByPCT() {
        ArrayList<Team> teams = selectAllTeams();

        // Sort the array of teams first by win percentage, then number of mercy wins, and finally by number of forfeit wins.
        teams.sort(Comparator.comparingDouble(Team::getWinPercentage).thenComparingInt(Team::getMercyWins).thenComparingInt(Team::getForfeitWins).reversed());

        return teams;
    }

    /**
     * Get a list of all unique team names in the league.
     * @return List of team names
     * @author BML
     * @since 20261111
     */
    public static List<String> getAllUniqueTeamNames() {
        return selectAllTeams().stream().map(Team::getTeamName).toList();
    }
}
