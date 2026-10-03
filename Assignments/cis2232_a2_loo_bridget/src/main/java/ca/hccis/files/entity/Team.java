package ca.hccis.files.entity;

import java.util.ArrayList;
import java.util.Objects;

public class Team {
    private String teamName;
    private ArrayList<Game> games = new ArrayList<>();

    public Team() {}

    public Team(String name) {
        this.teamName = name;
    }

    public Team(String name, Game game) {
        this.setTeamName(name);
        this.games.add(game);
    }

    public Team(String name, ArrayList<Game> games) {
        this.setTeamName(name);
        this.games = games;
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getGamesPlayed() {
        return this.games.size();
    }

    /**
     * Get the number of wins for this team from its collection of games.
     * @return The number of wins.
     */
    public int getWins() {
        int winCount = 0;
        for (Game game : this.games) if (game.getWinningTeam().equals(this.teamName)) winCount++;
        return winCount;
    }

    /**
     * Get the number of losses for this team from its collection of games.
     * @return The number of losses.
     */
    public int getLosses() {
        int lossCount = 0;
        for (Game game : this.games) if (!game.getWinningTeam().equals(this.teamName)) lossCount++;
        return lossCount;
    }

    /**
     * Calculate the win percentage for this team.
     * @return The win percentage.
     */
    public double getWinPercent() {
        return (double) this.getWins() / this.getGamesPlayed();
    }

    /**
     * Get the number of mercy wins for this team from its collection of games.
     * @return The number of mercy wins.
     */
    public int getMercyWins() {
        int mercyWinCount = 0;
        for (Game game : this.games) if (game.isMercyWin() && game.getWinningTeam().equals(this.teamName)) mercyWinCount++;
        return mercyWinCount;
    }

    public String toString() {
        return """
                Team Name: %s
                Wins: %d
                Losses: %d
                Mercy Wins: %d
                Games Played: %d
                Winning Percentage: %3f
                """.formatted(this.getTeamName(), this.getWins(), this.getLosses(), this.getMercyWins(), this.getGamesPlayed(), this.getWinPercent());
    }

    public void display() {
        System.out.println(this);
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
