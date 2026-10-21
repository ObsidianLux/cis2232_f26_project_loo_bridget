package ca.hccis.softball.bo;

import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.entity.Team;
import ca.hccis.softball.jpa.entity.Game;

import java.util.ArrayList;

public class TeamBO {
    public record Standing(int numberOfMercyWins, int numberOfWins, int numberOfLosses, double winPercentage, int numberOfForfeits) {}

    /**
     * Get details about a team's standing in the league, including the number of mercy rule wins, how many games they won, how many they lost, and what their win percentage is. CALCULATION
     * @param team The team to get details about
     * @return Standing details
     * @author BML
     * @since 20261020
     */
    public static Standing getTeamStanding(Team team) {
        int numberOfWins = 0;
        int numberOfLosses = 0;
        int numberOfMercyWins = 0;
        int numberOfForfeits = 0;

        // Loop through each game in the team's games property.
        for (Game game : team.getGames()) {
            if (game.getWinningTeam().equals(team.getTeamName())) {
                numberOfWins++;
                if (game.getIsMercyWin()) numberOfMercyWins++;
            }
            else {
                numberOfLosses++;
                if (game.getForfeited()) numberOfForfeits++;
            }
        }

        return new Standing(numberOfMercyWins, numberOfWins, numberOfLosses, (double) numberOfWins / team.getGames().size(), numberOfForfeits);
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
