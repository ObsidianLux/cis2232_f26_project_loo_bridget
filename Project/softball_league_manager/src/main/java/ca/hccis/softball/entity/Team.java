package ca.hccis.softball.entity;

import ca.hccis.softball.bo.TeamBO;
import ca.hccis.softball.jpa.entity.Game;

import java.util.ArrayList;
import java.util.Objects;

public class Team {
    private String teamName;
    private ArrayList<Game> games = new ArrayList<>();

    private int numberOfWins = 0;
    private int numberOfLosses = 0;
    private int numberOfMercyWins = 0;
    private int numberOfForfeits = 0;
    private double winPercentage = 0;

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
        return this.games.size();
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public int getNumberOfLosses() {
        return numberOfLosses;
    }

    public void setNumberOfLosses(int numberOfLosses) {
        this.numberOfLosses = numberOfLosses;
    }

    public int getNumberOfMercyWins() {
        return numberOfMercyWins;
    }

    public void setNumberOfMercyWins(int numberOfMercyWins) {
        this.numberOfMercyWins = numberOfMercyWins;
    }

    public int getNumberOfForfeits() {
        return numberOfForfeits;
    }

    public void setNumberOfForfeits(int numberOfForfeits) {
        this.numberOfForfeits = numberOfForfeits;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(double winPercentage) {
        this.winPercentage = winPercentage;
    }

    public void calculateStanding() {
        TeamBO.Standing standing = TeamBO.getTeamStanding(this);

        this.setNumberOfWins(standing.numberOfWins());
        this.setNumberOfLosses(standing.numberOfLosses());
        this.setNumberOfMercyWins(standing.numberOfMercyWins());
        this.setNumberOfForfeits(standing.numberOfForfeits());
        this.setWinPercentage(standing.winPercentage());
    }

    public String getWinPercentageString() {
        return String.format("%.3f", winPercentage);
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
