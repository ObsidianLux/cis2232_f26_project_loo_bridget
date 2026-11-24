package ca.hccis.softball.model.bo;

import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.model.domain.Team;
import ca.hccis.softball.model.dto.GameRequest;
import ca.hccis.softball.model.dto.InningRequest;
import ca.hccis.softball.model.entity.Game;

import java.time.LocalDate;
import java.util.*;

public class GameBO {
    /**
     * The number of innings required to apply the mercy rule.
     */
    public static final int MERCY_RULE_INNINGS = 5;
    public static final int MERCY_DIFFERENCE_MINIMUM = 10;
    public static final int MINIMUM_INNINGS_STANDARD = 7;

    /**
     * Get the name of the winning team.
     * @param game The game to check
     * @return The name of the winning team
     * @author BML
     * @since 20261025
     */
    public static String getWinningTeam(GameRequest game) {
        // If the game was forfeited, the winning team is the one that isn't at fault.
        if (game.forfeited() && game.teamAtFault() != null) return game.teamAtFault().equalsIgnoreCase(game.homeTeamName()) ? game.awayTeamName() : game.homeTeamName();

        // Game wasn't forfeited, so need to calculate the total number of runs for each team and compare.
        else return getHomeRuns(game) > getAwayRuns(game) ? game.homeTeamName() : game.awayTeamName();
    }

    /**
     * Get the score differential for a specific team in a game.
     * @param game The game to get the differential from
     * @param teamName The team to get the differential for
     * @return The score differential
     * @author BML
     * @since 20261103
     */
    public static String getPlusMinus(GameRequest game, String teamName) {
        // Make sure the game was played first.
        if (isPlayed(game.gameDay())) {
            // Get the winning team's name.
            boolean isWinner = teamName.equals(getWinningTeam(game));

            // Default to 7 which matches the difference if the game was forfeited.
            int scoreDifference = 7;

            // If the game wasn't forfeited, calculate the difference.
            if (!game.forfeited()) {
                // Get the absolute difference between the two scores.
                scoreDifference = Math.abs(getHomeRuns(game) - getAwayRuns(game));
            }

            // Return the difference in scores with a plus or minus prepended.
            return (isWinner ? "+" : "-") + scoreDifference;
        }

        return "-";
    }

    public static int getHomeRuns(GameRequest game) {
        List<InningRequest> innings = game.innings();

        if (innings == null) return 0;
        else return innings.stream()
                .mapToInt(InningRequest::homeRuns)
                .sum();
    }

    public static int getAwayRuns(GameRequest game) {
        List<InningRequest> innings = game.innings();

        if (innings == null) return 0;
        else return innings.stream()
                .mapToInt(InningRequest::awayRuns)
                .sum();
    }

    /**
     * Determine whether the game would have been played based on the date compared to today.
     * @param gameDay Thr date to compare
     * @return True if date is past or present, false if date is future.
     */
    public static boolean isPlayed(LocalDate gameDay) {
        // Use ! with .isAfter() so that true returns for played if the date is not in the future (i.e. after today).
        return !gameDay.isAfter(LocalDate.now());
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
            teams.computeIfAbsent(game.getHomeTeam(), Team::new).addGame(GameRequest.from(game));
            teams.computeIfAbsent(game.getAwayTeam(), Team::new).addGame(GameRequest.from(game));
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

        // TODO Consider likelihood of 3-way ties throwing exceptions.

        // Sort the array of teams first by win percentage. If there are any teams with the same PCT, place the team with more wins against the other first. Putting team two first in the comparisons will place the resulting list in descending order.
        teams.sort((teamOne, teamTwo) -> {
            // Sort by PCT first.
            int pctCompare = Double.compare(teamTwo.getWinPercentage(), teamOne.getWinPercentage());

            // If the win percentages are different, return the comparison result.
            if (pctCompare != 0) return pctCompare;

            // Tie-breaker: Compare head-to-head wins against each other.
            int teamOneWins = teamOne.getWinsAgainst(teamTwo);
            int teamTwoWins = teamTwo.getWinsAgainst(teamOne);

            return Integer.compare(teamTwoWins, teamOneWins);
        });

        // Get the leading team from the list.
        Team leadingTeam = teams.get(0);

        // Set the Games Behind value for each team using the leadingTeam.
        for (Team team : teams) team.setGamesBehind(leadingTeam);

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
