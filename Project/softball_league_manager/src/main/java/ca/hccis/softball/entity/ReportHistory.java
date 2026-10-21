package ca.hccis.softball.entity;

import ca.hccis.softball.bo.TeamBO;
import ca.hccis.softball.jpa.entity.Game;

import java.util.ArrayList;

/**
 * Entity class to hold the attributes of the reports.
 */
public class ReportHistory {
    private String team;
    private ArrayList<Team> teams;
    private ArrayList<Game> games;

    public ReportHistory() {
        this.teams = TeamBO.selectAllTeams();
    }

    public String getTeam() { return this.team; }

    public void setTeam(String team) { this.team = team; }

    public ArrayList<Game> getGames() { return this.games; }

    public void setGames(ArrayList<Game> games) { this.games = games; }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }
}
