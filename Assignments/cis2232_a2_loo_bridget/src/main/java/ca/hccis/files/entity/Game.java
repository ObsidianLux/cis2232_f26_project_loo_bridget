package ca.hccis.files.entity;

import ca.hccis.files.util.InputUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class Game {
    private int gameId;
    private String teamOneName;
    private String teamTwoName;

    private LinkedHashMap<Integer, Inning> innings = new LinkedHashMap<>();

    private int teamOneScore;
    private int teamTwoScore;

    private static final int MERCY_RULE_INNING = 5;

    public Game() {}

    public Game(int gameId, String teamOneName, String teamTwoName, LinkedHashMap<Integer, Inning> innings) {
        this.gameId = gameId;
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
        this.innings = innings;
    }

    public void getInformation() {
        this.gameId = InputUtility.getInputInt("Game Number (Must be unique): ", 0, -1, false);
        getGameInfo();
    }

    public void getInformation(int id) {
        this.setGameId(id);
        getGameInfo();
    }

    private void getGameInfo() {
        this.teamOneName = InputUtility.getInputString("First to-Bat Team: ", true, 1, -1);
        this.teamTwoName = InputUtility.getInputString("Last to-Bat Team: ", true, 1, -1);

        int inningCount = InputUtility.getInputInt("How many innings were in this game?", 0, -1, false);

        for (int inningNumber = 1; inningNumber <= inningCount; inningNumber++) {
            Inning inning = new Inning(inningNumber);
            inning.getInformation();

            // Create an inning object and add it to the HashMap for the game.
            this.innings.put(inningNumber, inning);
        }
    }

    public String getWinningTeam() {
        return this.teamOneScore > this.teamTwoScore ? this.teamOneName : this.teamTwoName;
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

    public int getScoreDifferential() {
        return Math.abs(this.teamOneScore - this.teamTwoScore);
    }

    public boolean isMercyWin() {
        return this.innings.size() == MERCY_RULE_INNING;
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

    public HashMap<Integer, Inning> getInnings() {
        return innings;
    }

    public void setInnings(LinkedHashMap<Integer, Inning> innings) {
        this.innings = innings;
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
