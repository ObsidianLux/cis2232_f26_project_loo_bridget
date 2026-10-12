package ca.hccis.files.entity;

import ca.hccis.files.util.GameUtility;

public class Inning {
    private int inningNumber;

    private int homeTeam;
    private int roadTeam;

    public Inning() {}

    public Inning(int inningNumber) {
        this.inningNumber = inningNumber;
    }

    public Inning(int inningNum, int homeTeam, int roadTeam) {
        this.inningNumber = inningNum;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
    }

    public void getInformation(GameUtility gameUtility) {
        boolean isValid;

        do {
            int roadRuns = gameUtility.getInputInt("Road team runs in inning #%d".formatted(this.inningNumber));

            if (roadRuns >= 0) {
                isValid = true;
                this.roadTeam = roadRuns;
            } else {
                isValid = false;
                // @modified 20261010 BML Uses gameUtility.display() instead of just println.
                gameUtility.display("Please provide a valid number of runs.");
            }
        } while (!isValid);

        do {
            int homeRuns = gameUtility.getInputInt("Home team runs in inning #%d".formatted(this.inningNumber));

            if (homeRuns >= 0) {
                isValid = true;
                this.homeTeam = homeRuns;
            } else {
                isValid = false;
                // @modified 20261010 BML Uses gameUtility.display() instead of just println.
                gameUtility.display("Please provide a valid number of runs.");
            }
        } while (!isValid);
    }

    public int getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(int roadTeam) {
        this.roadTeam = roadTeam;
    }

    public int getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(int homeTeam) {
        this.homeTeam = homeTeam;
    }

    public int getInningNumber() {
        return inningNumber;
    }

    public void setInningNumber(int inningNumber) {
        this.inningNumber = inningNumber;
    }
}
