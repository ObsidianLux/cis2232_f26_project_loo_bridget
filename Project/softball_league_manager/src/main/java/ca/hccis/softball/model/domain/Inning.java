package ca.hccis.softball.model.domain;

import com.google.gson.Gson;

import java.io.Serial;
import java.io.Serializable;

public class Inning implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int homeRuns;
    private int awayRuns;

    public Inning() {}

    public Inning(int homeRuns, int roadRuns) {
        this.homeRuns = homeRuns;
        this.awayRuns = roadRuns;
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

        this.homeRuns = inning.getHomeRuns();
        this.awayRuns = inning.getAwayRuns();
    }

    public int getAwayRuns() {
        return awayRuns;
    }

    public void setAwayRuns(int awayRuns) {
        this.awayRuns = awayRuns;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }
}
