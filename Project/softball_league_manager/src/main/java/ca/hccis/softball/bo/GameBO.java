package ca.hccis.softball.bo;

import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.entity.Inning;
import ca.hccis.softball.jpa.entity.Game;
import ca.hccis.softball.util.CisUtilityFile;

import java.util.ArrayList;

public class GameBO {
    public record GameResults(int winningTeam, boolean isMercyWin, int scoreDifferential) {}

    /**
     * Determine which team won the game based on how many runs their scored across all innings.
     * @param innings All the innings in the game.
     * @param homeTeam The name of the home team.
     * @param roadTeam The name of the road team.
     * @return The name of the winning team or an empty string if they tied.
     */
    public static String determineWinner(ArrayList<Inning> innings, String homeTeam, String roadTeam) {
        int homeRuns = 0;
        int roadRuns = 0;

        for (Inning inning : innings) {
            homeRuns += inning.getHomeTeamRuns();
            roadRuns += inning.getRoadTeamRuns();
        }

        if (homeRuns == roadRuns) return "";
        else return homeRuns > roadRuns ? homeTeam : roadTeam;
    }

    /**
     * Select all games that a team played in.
     * @param team The team to filter by.
     * @return A list of games.
     * @author BML
     * @since 20261021
     */
    public ArrayList<Game> processSelectAllByTeam(String team) {
        GameDAO gameDAO = new GameDAO();
        return gameDAO.selectAllByTeamName(team);
    }

    public static GameResults getGameResults(int homeTeamId, int roadTeamId, ArrayList<Inning> innings) {
        int homeRuns = 0;
        int roadRuns = 0;

        for (Inning inning : innings) {
            homeRuns += inning.getHomeTeamRuns();
            roadRuns += inning.getRoadTeamRuns();
        }

        int winningTeamId = homeRuns > roadRuns ? homeTeamId : roadTeamId;

        return new GameResults(winningTeamId, innings.size() == 5, Math.abs(homeRuns - roadRuns));
    }
}
