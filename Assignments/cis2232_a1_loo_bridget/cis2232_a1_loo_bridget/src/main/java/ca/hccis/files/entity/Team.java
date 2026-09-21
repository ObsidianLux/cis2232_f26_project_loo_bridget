package ca.hccis.files.entity;

import ca.hccis.files.util.InputUtility;

import java.util.Objects;

public class Team {
    private String teamName;
    private int wins;
    private int losses;
    private int mercyWins;

    public Team() {}

    public Team(String name) {
        this.teamName = name;
    }

    public Team(String name, int wins, int losses, int mercyWins) {
        this.setTeamName(name);
        this.setWins(wins);
        this.setLosses(losses);
        this.setMercyWins(mercyWins);
    }

    public void addWin() {
        this.wins++;
    }

    public void addLoss() {
        this.losses++;
    }

    public void addMercyWin() {
        this.mercyWins++;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public double getWinPercent() {
        return (double) this.wins / (this.wins + this.losses);
    }

    public int getGamesPlayed() {
        return this.wins + this.losses;
    }

    public int getMercyWins() {
        return mercyWins;
    }

    public void setMercyWins(int mercyWins) {
        this.mercyWins = mercyWins;
    }

    public String toString() {
        return """
                Team Name: %s
                Wins: %d
                Losses: %d
                Mercy Wins: %d
                Games Played: %d
                Winning Percentage: %3f
                """.formatted(this.getTeamName(), this.getWins(), this.getLosses(), this.getMercyWins(), this.getGamesPlayed(), this.getWinPercent());
    }

    public void display() {
        System.out.println(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Team team)) return false;
        return Objects.equals(getTeamName(), team.getTeamName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTeamName());
    }
}
