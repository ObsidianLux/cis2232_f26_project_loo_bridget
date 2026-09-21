package ca.hccis.files.entity;

import ca.hccis.files.util.InputUtility;

import java.util.Objects;

public class Game {
    private int gameId;
    private String teamOneName;
    private String teamTwoName;
    private int teamOneScore;
    private int teamTwoScore;
    private int scoreDifferential;
    private boolean isMercyWin;

    public Game() {}

    public Game(int gameId, String teamOneName, String teamTwoName, int teamOneScore, int teamTwoScore, int scoreDifferential, boolean isMercyWin) {
        this.gameId = gameId;
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;
        this.scoreDifferential = scoreDifferential;
        this.isMercyWin = isMercyWin;
    }

    public void getInformation() {
        this.gameId = InputUtility.getInputInt("Game ID: ", 0, -1, false);
        getGameInfo();
    }

    public void getInformation(int id) {
        this.setGameId(id);
        getGameInfo();
    }

    private void getGameInfo() {
        this.teamOneName = InputUtility.getInputString("Team 1 Name: ", true, 1, -1);
        this.teamTwoName = InputUtility.getInputString("Team 2 Name: ", true, 1, -1);
        this.teamOneScore = InputUtility.getInputInt("Team 1 Score: ", 1, -1, false);
        this.teamTwoScore = InputUtility.getInputInt("Team 2 Score: ", 1, -1, false);
        this.isMercyWin = InputUtility.getInputBoolean("Mercy rule applies?");
        this.setScoreDifferential();
    }

    public String getWinningTeam() {
        return this.getTeamOneScore() > this.getTeamTwoScore() ? this.getTeamOneName() : this.getTeamTwoName();
    }

    public String[] getTeamNames() {
        return new String[]{this.teamOneName, this.teamTwoName};
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTeamOneName() {
        return teamOneName;
    }

    public void setTeamOneName(String teamOneName) {
        this.teamOneName = teamOneName;
    }

    public String getTeamTwoName() {
        return teamTwoName;
    }

    public void setTeamTwoName(String teamTwoName) {
        this.teamTwoName = teamTwoName;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    public int getScoreDifferential() {
        return scoreDifferential;
    }

    public void setScoreDifferential() {
        this.scoreDifferential = Math.abs(this.teamOneScore - this.teamTwoScore);
    }

    public boolean isMercyWin() {
        return isMercyWin;
    }

    public void setMercyWin(boolean mercyWin) {
        isMercyWin = mercyWin;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game game)) return false;
        return getGameId() == game.getGameId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getGameId());
    }
}
