package ca.hccis.files.bo;

import ca.hccis.files.entity.Inning;

import java.util.ArrayList;

public class GameBO {
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
            homeRuns += inning.getHomeTeam();
            roadRuns += inning.getRoadTeam();
        }

        if (homeRuns == roadRuns) return "";
        else return homeRuns > roadRuns ? homeTeam : roadTeam;
    }
}
