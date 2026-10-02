package ca.hccis.files.bo;

import ca.hccis.files.entity.Game;
import ca.hccis.files.entity.Inning;

import java.util.ArrayList;

public class TeamBO {
    public static double determineStanding(String teamName, ArrayList<Game> games) {
        int winCount = 0;
        int lossCount = 0;

        for (Game game : games) {
            // Determine which team won this game.
            int teamOneScore = 0;
            int teamTwoScore = 0;

            for (Inning inning : game.getInnings().values()) {
                teamOneScore += inning.getTeamOneRuns();
                teamTwoScore += inning.getTeamTwoRuns();
            }

            String winningTeam = teamOneScore > teamTwoScore ? game.getTeamOneName() : game.getTeamTwoName();

            // Update win/loss counts.
            if (winningTeam.equals(teamName)) winCount++;
            else lossCount++;
        }

        // Calculate the win percentage.
        return (double) winCount / (winCount + lossCount);
    }
}
