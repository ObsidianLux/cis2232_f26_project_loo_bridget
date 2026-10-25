package ca.hccis.softball.entity;

import ca.hccis.softball.bo.GameBO;
import ca.hccis.softball.jpa.entity.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity class to hold the attributes of the reports.
 */
public class ReportHistory {
    private Team team;
    private String teamName;
    private ArrayList<Team> teams;


    public record HistoryReportData(String teamName, List<String> xAxisLabels, List<Integer> teamScoreDataSet, List<Integer> opponentScoreDataSet) {}

    public ReportHistory() {
        this.setTeams(GameBO.selectAllTeams());
    }

    public String getTeamName() { return this.teamName; }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<Game> getGames() {
        return this.getTeam() != null ? this.getTeam().getPlayedGames() : null;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeamByName() {
        for (Team team : this.teams) {
            if (team.getTeamName().equals(this.teamName)) return team;
        }
        return null;
    }

    public HistoryReportData getHistoryReportData() {
        List<String> xAxisLabels = new ArrayList<>();
        List<Integer> teamScoreDataSet = new ArrayList<>();
        List<Integer> opponentScoreDataSet = new ArrayList<>();

        // Only include played games in the report.
        ArrayList<Game> games = this.getGames().stream().filter(Game::getPlayed).collect(Collectors.toCollection(ArrayList::new));

        for (Game game : games) {
            boolean teamIsHome = this.getTeamName().equals(game.getHomeTeam());

            xAxisLabels.add("%s (%s)".formatted(getOpponentTeamName(game.getHomeTeam(), game.getRoadTeam()), game.getDate().toString()));

            if (teamIsHome) {
                teamScoreDataSet.add(game.getHomeScore());
                opponentScoreDataSet.add(game.getRoadScore());
            } else {
                teamScoreDataSet.add(game.getRoadScore());
                opponentScoreDataSet.add(game.getHomeScore());
            }
        }

        return new HistoryReportData(this.getTeamName(), xAxisLabels, teamScoreDataSet, opponentScoreDataSet);
    }

    public String getOpponentTeamName(String homeTeamName, String roadTeamName) {
        return homeTeamName.equals(this.getTeamName()) ? roadTeamName : homeTeamName;
    }
}
