package ca.hccis.softball.bo;

import ca.hccis.softball.dao.GameDAO;
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

    public static String getPlusMinus(Game game, String teamName) {
        boolean isWinner = teamName.equals(getWinningTeam(game));
        int scoreDifference = Math.abs(game.getHomeScore() - game.getRoadScore());
        return (isWinner ? "+" : "-") + scoreDifference;
    }

    /**
     * Select all games that a team played in.
     * @param team The team to filter by.
     * @return A list of games.
     * @author BML
     * @since 20261021
     */
    public static ArrayList<Game> selectAllByTeam(String team) {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.selectAllByTeamName(team);
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
