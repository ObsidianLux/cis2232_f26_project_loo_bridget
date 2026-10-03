package ca.hccis.files.entity;

import ca.hccis.files.util.InputUtility;

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

    public void getInformation() {
        boolean isValid;

        do {
            int roadRuns = InputUtility.getInputInt("Road team runs in inning #%d".formatted(this.inningNumber));

            if (roadRuns >= 0) {
                isValid = true;
                this.roadTeam = roadRuns;
            } else {
                isValid = false;
                System.out.println("Please provide a valid number of runs.");
            }
        } while (!isValid);

        do {
            int homeRuns = InputUtility.getInputInt("Home team runs in inning #%d".formatted(this.inningNumber));

            if (homeRuns >= 0) {
                isValid = true;
                this.roadTeam = homeRuns;
            } else {
                isValid = false;
                System.out.println("Please provide a valid number of runs.");
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
