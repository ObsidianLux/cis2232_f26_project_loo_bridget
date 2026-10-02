package ca.hccis.files.entity;

import ca.hccis.files.util.InputUtility;

public class Inning {
    private int inningNumber;

    private int teamOneRuns;
    private int teamTwoRuns;

    public Inning() {}

    public Inning(int inningNumber) {
        this.inningNumber = inningNumber;
    }

    public Inning(int inningNum, int teamOneRuns, int teamTwoRuns) {
        this.inningNumber = inningNum;
        this.teamOneRuns = teamOneRuns;
        this.teamTwoRuns = teamTwoRuns;
    }

    public void getInformation() {
        // Get the number of runs for each team in this inning.
        this.teamOneRuns = InputUtility.getInputInt("First at-Bat Number of Runs for Inning #%d: ".formatted(inningNumber), 0, -1, false);
        this.teamTwoRuns = InputUtility.getInputInt("Last at-Bat Number of Runs for Inning #%d: ".formatted(inningNumber), 0, -1, false);
    }

    public int getTeamTwoRuns() {
        return teamTwoRuns;
    }

    public void setTeamTwoRuns(int teamTwoRuns) {
        this.teamTwoRuns = teamTwoRuns;
    }

    public int getTeamOneRuns() {
        return teamOneRuns;
    }

    public void setTeamOneRuns(int teamOneRuns) {
        this.teamOneRuns = teamOneRuns;
    }

    public int getInningNumber() {
        return inningNumber;
    }

    public void setInningNumber(int inningNumber) {
        this.inningNumber = inningNumber;
    }
}
