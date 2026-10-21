package ca.hccis.softball.entity;

import com.google.gson.Gson;

public class Inning {
    private int inningNumber;

    private int homeTeamRuns;
    private int roadTeamRuns;

    public Inning() {}

    public Inning(int inningNumber) {
        this.inningNumber = inningNumber;
    }

    public Inning(int inningNum, int homeTeamRuns, int roadTeamRuns) {
        this.inningNumber = inningNum;
        this.homeTeamRuns = homeTeamRuns;
        this.roadTeamRuns = roadTeamRuns;
    }

    /**
     * Constructor that uses a json string.
     * @param json The JSON string
     * @author BML
     * @since 20261020
     */
    public Inning(String json) {
        Gson gson = new Gson();
        Inning inning = gson.fromJson(json, Inning.class);

        this.inningNumber = inning.getInningNumber();
        this.homeTeamRuns = inning.getHomeTeamRuns();
        this.roadTeamRuns = inning.getRoadTeamRuns();
    }

    public int getRoadTeamRuns() {
        return roadTeamRuns;
    }

    public void setRoadTeamRuns(int roadTeamRuns) {
        this.roadTeamRuns = roadTeamRuns;
    }

    public int getHomeTeamRuns() {
        return homeTeamRuns;
    }

    public void setHomeTeamRuns(int homeTeamRuns) {
        this.homeTeamRuns = homeTeamRuns;
    }

    public int getInningNumber() {
        return inningNumber;
    }

    public void setInningNumber(int inningNumber) {
        this.inningNumber = inningNumber;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
