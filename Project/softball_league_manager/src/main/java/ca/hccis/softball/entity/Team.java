package ca.hccis.softball.entity;

import ca.hccis.softball.bo.GameBO;
import ca.hccis.softball.jpa.entity.Game;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Team {
    private String teamName;
    private ArrayList<Game> games = new ArrayList<>();

    public record Wins(int total, int mercy, int forfeit) {}
    public record Losses(int total, int mercy, int forfeit) {}

    private Wins wins = new Wins(0, 0, 0);
    private Losses losses = new Losses(0, 0, 0);

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
    public Team(String name, Game game) {
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
    public Team(String name, ArrayList<Game> games) {
        this.setTeamName(name);
        this.setGames(games);
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public ArrayList<Game> getPlayedGames() {
        return this.games.stream().filter(Game::getPlayed).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
        this.calculateStanding();
    }

    public void addGame(Game game) {
        this.games.add(game);
        this.calculateStanding();
    }

    public void removeGame(Game game) {
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
        return (this.getNumberOfGamesPlayed() == 0 ? 0 : (double) this.getWins().total / this.getNumberOfGamesPlayed());
    }

    public String getWinPercentageString() {
        // The decimal followed by three zeros forces exactly 3 decimal spots without anything before the decimal.
        DecimalFormat df = new DecimalFormat(".000");
        return df.format(this.getWinPercentage());
    }

    public Wins getWins() {
        return wins;
    }

    public void setWins(Wins wins) {
        this.wins = wins;
    }

    public Losses getLosses() {
        return losses;
    }

    public void setLosses(Losses losses) {
        this.losses = losses;
    }

    public int getMercyWins() {
        return this.getWins().mercy;
    }

    public int getForfeitWins() {
        return this.getWins().forfeit;
    }

    public void calculateStanding() {
        int numberOfWins = 0;
        int numberOfMercyWins = 0;
        int numberOfForfeitWins = 0;

        int numberOfLosses = 0;
        int numberOfMercyLosses = 0;
        int numberOfForfeitLosses = 0;

        // Loop through each game in the team's games property.
        for (Game game : this.getPlayedGames()) {
            if (GameBO.getWinningTeam(game).equals(this.getTeamName())) {
                numberOfWins++;
                if (game.getWasMercyWin()) numberOfMercyWins++;
                else if (game.getWasForfeited()) numberOfForfeitWins++;
            }
            else {
                numberOfLosses++;
                if (game.getWasForfeited()) numberOfForfeitLosses++;
                else if (game.getWasMercyWin()) numberOfMercyLosses++;
            }
        }

        this.setWins(new Wins(numberOfWins, numberOfMercyWins, numberOfForfeitWins));
        this.setLosses(new Losses(numberOfLosses, numberOfMercyLosses, numberOfForfeitLosses));
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Name: %s; Games: ".formatted(this.getTeamName()));

        for (Game game : this.getGames()) {
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
