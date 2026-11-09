package ca.hccis.softball.bo;

import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.entity.Inning;
import ca.hccis.softball.entity.Team;
import ca.hccis.softball.jpa.entity.Game;

import java.util.ArrayList;

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
     * @param innings The innings in the game
     * @return The updated game
     * @author BML
     * @since 20261107
     */
    public static Game setPlayNotForfeit(Game game, ArrayList<Inning> innings) {
        game.setPlayed(true);
        game.setWasForfeited(false);
        game.setInnings(innings);

        int homeScore = 0;
        int roadScore = 0;

        for (Inning inning : innings) {
            homeScore += inning.getHomeRuns();
            roadScore += inning.getRoadRuns();
        }

        game.setHomeScore(homeScore);
        game.setRoadScore(roadScore);

        game.setWasMercyWin(innings.size() == 5);

        return game;
    }

    /**
     * Get a list of all unique teams in the league.
     * @return List of teams
     * @author BML
     * @since 20261020
     */
    public static ArrayList<Team> selectAllTeams() {
        ArrayList<Game> games = new GameDAO().selectAll();
        ArrayList<Team> teams = new ArrayList<>();

        if (!games.isEmpty()) {
            for (Game game : games) {
                // If the teams list is empty, both the home team and road team can be added without further checks.
                if (teams.isEmpty()) {
                    teams.add(new Team(game.getHomeTeam(), game));
                    teams.add(new Team(game.getRoadTeam(), game));
                } else {
                    // Check the teams list for the home team.
                    Team homeTeam = new Team(game.getHomeTeam(), game);
                    int homeTeamIndex = teams.indexOf(homeTeam);

                    // If the home team isn't on the list, add it. Otherwise, add the game to the existing element.
                    if (homeTeamIndex == -1) teams.add(homeTeam);
                    else teams.get(homeTeamIndex).addGame(game);

                    // If the road team isn't on the list, add it. Otherwise, add the game to the existing element.
                    Team roadTeam = new Team(game.getRoadTeam(), game);
                    int roadTeamIndex = teams.indexOf(roadTeam);

                    if (roadTeamIndex == -1) teams.add(roadTeam);
                    else teams.get(roadTeamIndex).addGame(game);
                }
            }
        }

        // Returns an empty array if no teams were found.
        return teams;
    }
}
