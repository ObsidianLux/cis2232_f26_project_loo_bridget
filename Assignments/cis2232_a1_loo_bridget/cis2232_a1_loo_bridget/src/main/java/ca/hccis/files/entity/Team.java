package ca.hccis.files.entity;

import ca.hccis.files.util.InputUtility;

public class Team {
    private int id;
    private String teamName;
    private int wins;
    private int losses;
    private int mercyWins;
    private int scoreDifferential;
    private double winRate;
    private int gamesPlayed;

    // TODO DONE Do the scoreDifferential, winRate, and gamesPlayed attributes need to be provided by the user or can they just be calculated by the program?
    // gamesPlayed can be calculated, but not scoreDifferential or winRate.

    public Team() {}

    public Team(int id, String name, int wins, int losses, int mercyWins, int scoreDiff, double winRate, int games) {
        this.id = id;
        this.teamName = name;
        this.wins = wins;
        this.losses = losses;
        this.mercyWins = mercyWins;
        this.scoreDifferential = scoreDiff;
        this.winRate = winRate;
        this.gamesPlayed = games;
    }

    public void getInformation() {
        this.teamName = InputUtility.getInputString("Team Name: ", true, -1, -1);
        this.wins = InputUtility.getInputInt("Number of Wins: ", 0, -1, false);
        this.losses = InputUtility.getInputInt("Number of Losses: ", 0, -1, false);
        this.mercyWins = InputUtility.getInputInt("Number of Mercy Wins: ", 0, -1, false);
        this.scoreDifferential = InputUtility.getInputInt("Score Differential", -1, -1, false);
        this.winRate = InputUtility.getInputDouble("Win Percentage", -1, -1, false);
        this.setGamesPlayed();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getScoreDifferential() {
        return scoreDifferential;
    }

    public void setScoreDifferential(int scoreDifferential) {
        this.scoreDifferential = scoreDifferential;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed() {
        this.gamesPlayed = this.wins + this.losses;
    }

    public int getMercyWins() {
        return mercyWins;
    }

    public void setMercyWins(int mercyWins) {
        this.mercyWins = mercyWins;
    }

    public String toString() {
        // TODO DONE Should the win rate be whole number or decimal? BA: Three decimal places.
        return """
                Team Name: %s
                Wins: %d
                Losses: %d
                Mercy Wins: %d
                Games Played: %d
                Score Differential: %d
                Win Rate: %3f
                """.formatted(this.teamName, this.wins, this.losses, this.mercyWins, this.gamesPlayed, this.scoreDifferential, this.winRate);
    }
}
