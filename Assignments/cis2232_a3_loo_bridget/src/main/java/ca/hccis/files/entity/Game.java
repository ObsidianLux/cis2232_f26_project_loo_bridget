package ca.hccis.files.entity;

import ca.hccis.files.bo.GameBO;
import ca.hccis.files.util.GameUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class Game {
    private int gameId;
    private String homeTeam;
    private String roadTeam;
    private boolean forfeited = false;
    private String teamAtFault;
    private String winningTeam;
    private boolean isMercyWin = false;

    private static final int MERCY_RULE_INNING = 5;
    private static final int STANDARD_GAME_LENGTH = 7;

    // @modified 20261010 BML Transient GameUtility to that it doesn't get serialized and saved in the json file.
    private transient GameUtility gameUtility = new GameUtility();

    public Game() {}

    public Game(GameUtility gameUtility) {
        this.gameUtility = gameUtility;
    }

    public Game(int id, GameUtility gameUtility) {
        this.gameId = id;
        this.gameUtility = gameUtility;
    }

    /**
     * This constructor exists for checking equality. Don't use it for anything else.
     */
    public Game(String homeTeam, String roadTeam) {
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
    }

    public Game(int id, String homeTeam, String roadTeam, GameUtility gameUtility) {
        this.gameId = id;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.gameUtility = gameUtility;
    }

    public Game(int gameId, String homeTeam, String roadTeam, String winningTeam, boolean isMercyWin, GameUtility gameUtility) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.winningTeam = winningTeam;
        this.isMercyWin = isMercyWin;
        this.gameUtility = gameUtility;
    }

    public Game(int gameId, String homeTeam, String roadTeam, boolean forfeited, String teamAtFault, String winningTeam, boolean isMercyWin, GameUtility gameUtility) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.winningTeam = winningTeam;
        this.isMercyWin = isMercyWin;
        this.forfeited = forfeited;
        this.teamAtFault = teamAtFault;
        this.gameUtility = gameUtility;
    }

    public void getInformation(int id) {
        this.setGameId(id);

        // Get the names of each team.
        this.setTeamNames();

        this.getGameInfo();
    }

    public void getInformation(int id, String homeTeam, String roadTeam) {
        this.setGameId(id);

        this.setHomeTeam(homeTeam);
        this.setRoadTeam(roadTeam);

        getGameInfo();
    }

    /**
     * Used by the getInformation methods to get the rest of the game's info from the user.
     * @since 20261002
     * @author BML
     * @modified 20261010 BML Made public.
     */
    public void getGameInfo() {
        // @modified 20261010 BML Ask the user if the game was forfeited. Default to false if they canceled.
        this.setForfeited(gameUtility.getInputBoolean("Was this game forfeited?").orElse(false));
        if (this.forfeited) {
            boolean isValidTeam = false;

            do {
                // Get the name of the team that forfeited.
                String team = gameUtility.getInputString("Team At Fault: ", true);

                if (team.equalsIgnoreCase(this.homeTeam) || team.equalsIgnoreCase(this.roadTeam)) {
                    isValidTeam = true;
                    this.setTeamAtFault(team);

                    // The winning team is the one that didn't forfeit.
                    String winningTeam = team.equalsIgnoreCase(this.homeTeam) ? this.roadTeam : this.homeTeam;
                    this.setWinningTeam(winningTeam);
                } else if (team.equals(GameUtility.CANCEL)) {
                    // @modified 20261010 BML If the user canceled, set forfeited back to false and continue collecting info.
                    this.forfeited = false;
                    break;
                } else {
                    // @modified 20261010 BML Uses gameUtility.display() instead of just println.
                    gameUtility.display("Input does not match one of the teams playing in this game. Please try again.");
                }
            } while (!isValidTeam);
        }

        // @modified 20261010 BML Split the following code into a separate if statement.
        if (!this.forfeited) {
            // Ask the user if the game ended because of the mercy rule. Default to false if the user canceled.
            this.isMercyWin = gameUtility.getInputBoolean("Was this game won because of the Mercy Rule?").orElse(false);

            int numberOfInnings = this.isMercyWin ? MERCY_RULE_INNING : STANDARD_GAME_LENGTH;

            // @modified 20261010 BML Uses gameUtility.display() instead of just println.
            gameUtility.display("Please provide the number of runs that each team scored in each inning.");
            ArrayList<Inning> innings = new ArrayList<>();

            for (int inningNumber = 1; inningNumber <= numberOfInnings; inningNumber++) innings.add(createInning(inningNumber));

            this.winningTeam = GameBO.determineWinner(innings, this.homeTeam, this.roadTeam);

            if (this.winningTeam.isEmpty()) {
                int tiebreakerNumber = STANDARD_GAME_LENGTH + 1;

                do {
                    // @modified 20261010 BML Uses gameUtility.display() instead of just println.
                    gameUtility.display("The score is tied, so play continues until the tie is broken.");
                    innings.add(createInning(tiebreakerNumber));
                    tiebreakerNumber++;
                    this.winningTeam = GameBO.determineWinner(innings, this.homeTeam, this.roadTeam);
                } while (this.winningTeam.isEmpty());
            }
        }
    }

    /**
     * Create and return an inning after getting info about it from the user.
     * @param inningNumber The inning's unique identifier (e.g. inning 1, inning 5, etc.).
     * @return The inning
     */
    private Inning createInning(int inningNumber) {
        Inning inning = new Inning(inningNumber);
        inning.getInformation(gameUtility);
        return inning;
    }

    public void setTeamNames() {
        this.setHomeTeam();
        this.setRoadTeam();
    }

    public String[] getTeamNames() {
        return new String[]{this.homeTeam, this.roadTeam};
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setHomeTeam() {
        this.homeTeam = gameUtility.getInputString("Home Team: ", true);
    }

    public String getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(String roadTeam) {
        this.roadTeam = roadTeam;
    }

    public void setRoadTeam() {
        this.roadTeam = gameUtility.getInputString("Road Team: ", true);
    }

    public boolean isForfeited() {
        return forfeited;
    }

    public void setForfeited(boolean forfeited) {
        this.forfeited = forfeited;
    }

    public String getTeamAtFault() {
        return teamAtFault;
    }

    public void setTeamAtFault(String teamAtFault) {
        this.teamAtFault = teamAtFault;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    public boolean isMercyWin() {
        return isMercyWin;
    }

    public void setMercyWin(boolean mercyWin) {
        isMercyWin = mercyWin;
    }

    /**
     * Use Gson to serialize this instance in JSON format.
     * @return JSON-formatted string.
     * @author BML
     * @since 20261010
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game game)) return false;
        return (Objects.equals(getHomeTeam(), game.getHomeTeam()) && Objects.equals(getRoadTeam(), game.getRoadTeam())) || (Objects.equals(getHomeTeam(), game.getRoadTeam()) && Objects.equals(getRoadTeam(), game.getHomeTeam()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeTeam(), getRoadTeam());
    }
}
