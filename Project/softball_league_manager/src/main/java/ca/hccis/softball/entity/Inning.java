package ca.hccis.softball.entity;

import com.google.gson.Gson;

public class Inning {
    private int inningNumber;

    private int homeRuns;
    private int roadRuns;

    public Inning() {}

    public Inning(int inningNumber) {
        this.inningNumber = inningNumber;
    }

    public Inning(int inningNum, int homeRuns, int roadRuns) {
        this.inningNumber = inningNum;
        this.homeRuns = homeRuns;
        this.roadRuns = roadRuns;
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
        this.homeRuns = inning.getHomeRuns();
        this.roadRuns = inning.getRoadRuns();
    }

    public int getRoadRuns() {
        return roadRuns;
    }

    public void setRoadRuns(int roadRuns) {
        this.roadRuns = roadRuns;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public int getInningNumber() {
        return inningNumber;
    }

    public void setInningNumber(int inningNumber) {
        this.inningNumber = inningNumber;
    }
}
