package ca.hccis.softball.model.domain;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.dto.GameRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class to hold the attributes of the team history report.
 */
public class ReportHistory {
    public record HistoryReportData(String teamName, List<String> xAxisLabels, List<Integer> teamScoreDataSet, List<Integer> opponentScoreDataSet) {}

    private Team team;
    private String teamName;
    private List<Team> teams;

    public ReportHistory() {
        this.setTeams(GameBO.selectAllTeams());
    }

    public String getTeamName() { return this.teamName; }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Get a list of games played by the team.
     * @return List of played games or an empty list if none have been played.
     */
    public List<GameRequest> getGames() {
        return this.getTeam() != null ? this.getTeam().getPlayedGames() : new ArrayList<>();
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Get a Team object from the teams list by matching the name in the teamName attribute.
     * @return The team
     * @author BML
     * @since 20261105
     */
    public Team getTeamByName() {
        for (Team team : this.teams) {
            if (team.getTeamName().equals(this.teamName)) return team;
        }
        return null;
    }

    /**
     * Get the datasets and x-axis labels for the Team History report chart. This method will likely appear to be unused, but it gets called in the script element in history.html.
     * @return The datasets and labels.
     * @author BML
     * @since 20261030
     */
    public HistoryReportData getHistoryReportData() {
        List<String> xAxisLabels = new ArrayList<>();
        List<Integer> teamScoreDataSet = new ArrayList<>();
        List<Integer> opponentScoreDataSet = new ArrayList<>();

        // Only include played games in the report.
        List<GameRequest> games = this.getGames().stream().filter(GameRequest::played).toList();

        // Loop through the played games.
        for (GameRequest game : games) {
            // Determine whether the team being reported on is the home team or road team for this game.
            boolean teamIsHome = this.getTeamName().equals(game.homeTeamName());

            xAxisLabels.add("%s (%s)".formatted(getOpponentTeamName(game.homeTeamName(), game.awayTeamName()), game.gameDay().toString()));

            if (teamIsHome) {
                teamScoreDataSet.add(game.homeRuns());
                opponentScoreDataSet.add(game.awayRuns());
            } else {
                teamScoreDataSet.add(game.awayRuns());
                opponentScoreDataSet.add(game.homeRuns());
            }
        }

        return new HistoryReportData(this.getTeamName(), xAxisLabels, teamScoreDataSet, opponentScoreDataSet);
    }

    public String getOpponentTeamName(String homeTeamName, String awayTeamName) {
        return homeTeamName.equals(this.getTeamName()) ? awayTeamName : homeTeamName;
    }
}
