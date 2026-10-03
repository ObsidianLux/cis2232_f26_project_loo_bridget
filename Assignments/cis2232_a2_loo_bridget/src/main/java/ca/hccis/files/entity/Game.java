package ca.hccis.files.entity;

import ca.hccis.files.bo.GameBO;
import ca.hccis.files.util.InputUtility;

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

    public Game() {}

    public Game(int id) {
        this.gameId = id;
    }

    public Game(int id, String homeTeam, String roadTeam) {
        this.gameId = id;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
    }

    public Game(int gameId, String homeTeam, String roadTeam, String winningTeam, boolean isMercyWin) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.winningTeam = winningTeam;
        this.isMercyWin = isMercyWin;
    }

    public Game(int gameId, String homeTeam, String roadTeam, boolean forfeited, String teamAtFault, String winningTeam, boolean isMercyWin) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.winningTeam = winningTeam;
        this.isMercyWin = isMercyWin;
        this.forfeited = forfeited;
        this.teamAtFault = teamAtFault;
    }

    public void getInformation(int id) {
        this.setGameId(id);

        // Get the names of each team.
        this.setHomeTeam(InputUtility.getInputString("Home Team: ", true, 1, -1));
        this.setRoadTeam(InputUtility.getInputString("Road Team: ", true, 1, -1));

        this.getGameInfo();
    }

    public void getInformation(int id, String homeTeam, String roadTeam) {
        this.setGameId(id);

        this.setHomeTeam(homeTeam);
        this.setRoadTeam(roadTeam);

        getGameInfo();
    }

    private void getGameInfo() {
        // Ask the user if the game was forfeited.
        this.setForfeited(InputUtility.getInputBoolean("Was this game forfeited?"));
        if (this.forfeited) {
            boolean isValidTeam = false;

            do {
                // Get the name of the team that forfeited.
                String team = InputUtility.getInputString("Team At Fault: ", true, 1, -1);

                if (team.equalsIgnoreCase(this.homeTeam) || team.equalsIgnoreCase(this.roadTeam)) {
                    isValidTeam = true;
                    this.setTeamAtFault(team);

                    // The winning team is the one that didn't forfeit.
                    String winningTeam = team.equalsIgnoreCase(this.homeTeam) ? this.homeTeam : this.roadTeam;
                    this.setWinningTeam(winningTeam);
                } else {
                    System.out.println("Input does not match one of the teams playing in this game. Please try again.");
                }
            } while (!isValidTeam);
        } else {
            // Ask the user if the game ended because of the mercy rule.
            this.isMercyWin = InputUtility.getInputBoolean("Was this game won because of the Mercy Rule?");

            int numberOfInnings = this.isMercyWin ? MERCY_RULE_INNING : STANDARD_GAME_LENGTH;

            System.out.println("Please provide the number of runs that each team scored in each inning.");
            ArrayList<Inning> innings = new ArrayList<>();

            for (int inningNumber = 1; inningNumber <= numberOfInnings; inningNumber++) innings.add(createInning(inningNumber));

            this.winningTeam = GameBO.determineWinner(innings, this.homeTeam, this.roadTeam);

            if (this.winningTeam.isEmpty()) {
                int tiebreakerNumber = STANDARD_GAME_LENGTH + 1;

                do {
                    System.out.println("The score is tied, so play continues until the tie is broken.");
                    innings.add(createInning(tiebreakerNumber));
                    tiebreakerNumber++;
                    this.winningTeam = GameBO.determineWinner(innings, this.homeTeam, this.roadTeam);
                } while (this.winningTeam.isEmpty());
            }
        }
    }

    private Inning createInning(int inningNumber) {
        Inning inning = new Inning(inningNumber);
        inning.getInformation();
        return inning;
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

    public String getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(String roadTeam) {
        this.roadTeam = roadTeam;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game game)) return false;
        return Objects.equals(getHomeTeam(), game.getHomeTeam()) && Objects.equals(getRoadTeam(), game.getRoadTeam());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeTeam(), getRoadTeam());
    }
}
