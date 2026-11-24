package ca.hccis.softball.model.domain;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.dto.GameRequest;

import java.text.DecimalFormat;
import java.util.*;

public class Team {
    // Record for storing the number of games won and lost from the last 5 games.
    public record L5(int gamesWon, int gamesLost) {}

    // Record for the number of games won and lost in a particular stadium (home/road).
    public record StadiumRecord(int wins, int losses) {}

    private String teamName;
    private List<GameRequest> games = new ArrayList<>();
    private int wins = 0;
    private int losses = 0;

    // Keys are opponent team names, values are number of wins against that team.
    public Map<String, Integer> headToHeadWins = new HashMap<>();

    // The Games Behind value for this team compared to the team that is leading in the standings. Set to 0 if this is the leading team.
    public double gamesBehind = 0;

    public Team() {}

    public Team(String name) {
        this.teamName = name;
    }

    /**
     * Constructor to set the team name and add a single game.
     * @param name Team name
     * @param game Game to add
     * @author BML
     * @since 20261020
     */
    public Team(String name, GameRequest game) {
        this.setTeamName(name);
        this.addGame(game);
    }

    /**
     * Constructor to set the team name and the list of games.
     * @param name Team name
     * @param games List of games
     * @author BML
     * @since 20261020
     */
    public Team(String name, List<GameRequest> games) {
        this.setTeamName(name);
        this.setGames(games);
    }

    public List<GameRequest> getGames() {
        return this.games;
    }

    public List<GameRequest> getPlayedGames() {
        return this.games.stream().filter(GameRequest::played).toList();
    }

    public void setGames(List<GameRequest> games) {
        this.games = games;
        this.calculateStanding();
    }

    public void addGame(GameRequest game) {
        this.games.add(game);
        this.calculateStanding();
    }

    public void removeGame(GameRequest game) {
        this.games.remove(game);
        this.calculateStanding();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getNumberOfGamesPlayed() {
        return this.getPlayedGames().size();
    }

    public int getNumberOfGamesScheduled() { return this.games.size(); }

    public double getWinPercentage() {
        return (this.getNumberOfGamesPlayed() == 0 ? 0 : (double) this.wins / this.getNumberOfGamesPlayed());
    }

    public String getWinPercentageString() {
        // The decimal followed by three zeros forces exactly 3 decimal spots without anything before the decimal.
        DecimalFormat df = new DecimalFormat(".000");
        return df.format(this.getWinPercentage());
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public Map<String, Integer> getHeadToHeadWins() {
        return headToHeadWins;
    }

    public void setHeadToHeadWins(Map<String, Integer> headToHeadWins) {
        this.headToHeadWins = headToHeadWins;
    }

    public double getGamesBehind() {
        return gamesBehind;
    }

    public void setGamesBehind(double gamesBehind) {
        this.gamesBehind = gamesBehind;
    }

    public void setGamesBehind(Team leadingTeam) {
        // If this is the leading team, Games Behind is 0.
        if (this.equals(leadingTeam)) this.gamesBehind = 0;
        else this.gamesBehind = (double) ((leadingTeam.getWins() - leadingTeam.getLosses()) - (this.getWins() - this.getLosses())) / 2;
    }

    /**
     * Get the record of games won and lost for the team's 5 most recently played games.
     * @return Games won and lost from the last 5 games
     */
    public L5 getL5() {
        int numberOfWins = 0;
        int numberOfLosses = 0;

        // Get the 5 most recently played games.
        List<GameRequest> mostRecentGames = this.getPlayedGames().stream()
                .sorted(Comparator.comparing(GameRequest::gameDay).reversed())
                .limit(5)
                .toList();

        for (GameRequest game : mostRecentGames) {
            if (game.winningTeamName().equals(this.getTeamName())) numberOfWins++;
            else numberOfLosses++;
        }

        return new L5(numberOfWins, numberOfLosses);
    }

    public StadiumRecord getHomeRecord() {
        return getStadiumRecord("home");
    }

    public StadiumRecord getRoadRecord() {
        return getStadiumRecord("road");
    }

    private StadiumRecord getStadiumRecord(String stadium) {
        // Get a filtered list of games so that only those where this team was the specified team are included.
        List<GameRequest> stadiumGames = this.getPlayedGames().stream()
                .filter((game) -> this.getTeamName().equals(stadium.equals("home") ? game.homeTeamName() : game.awayTeamName()))
                .toList();

        int numberOfWins = 0;
        int numberOfLosses = 0;

        for (GameRequest game : stadiumGames) {
            // Check whether this team won the game or not and update the variables.
            if (game.winningTeamName().equals(this.getTeamName())) numberOfWins++;
            else numberOfLosses++;
        }

        return new StadiumRecord(numberOfWins, numberOfLosses);
    }

    /**
     * Get the number of games this team has won against a specific opponent.
     * @param opponent The team to check for.
     * @return The number of wins against the opponent.
     */
    public int getWinsAgainst(Team opponent) {
        return this.getHeadToHeadWins().getOrDefault(opponent.getTeamName(), 0);
    }

    public void calculateStanding() {
        int numberOfWins = 0;

        int numberOfLosses = 0;
        
        Map<String, Integer> headToHeadWinsMap = new HashMap<>();

        // Loop through each game in the team's games property.
        for (GameRequest game : this.getPlayedGames()) {
            if (GameBO.getWinningTeam(game).equals(this.getTeamName())) {
                numberOfWins++;
                String opponentName = game.homeTeamName().equals(this.teamName) ? game.awayTeamName() : game.homeTeamName();

                // Get the current number of wins against this opponent and add 1. Default to 0 + 1 if this team has never won against this opponent before.
                int numberOfWinsAgainstOpponent = this.headToHeadWins.getOrDefault(opponentName, 0) + 1;

                // Update the map by replacing an existing value or adding a new key-value pair.
                headToHeadWinsMap.put(opponentName, numberOfWinsAgainstOpponent);
            }
            else numberOfLosses++;
        }

        this.setWins(numberOfWins);
        this.setLosses(numberOfLosses);
        this.setHeadToHeadWins(headToHeadWinsMap);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Name: %s; Games: ".formatted(this.getTeamName()));

        for (GameRequest game : this.getGames()) {
            output.append(game.toString());
        }

        return output.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Team team)) return false;
        return Objects.equals(getTeamName(), team.getTeamName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTeamName());
    }
}
