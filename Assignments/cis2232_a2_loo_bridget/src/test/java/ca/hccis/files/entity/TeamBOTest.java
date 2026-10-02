package ca.hccis.files.entity;

import ca.hccis.files.bo.TeamBO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;

public class TeamBOTest {
    @Test
    void testDetermineStanding_2Wins_1Loss() {
        Team team = new Team("Apples");

        LinkedHashMap<Integer, Inning> inningsGame1 = new LinkedHashMap<>();
        inningsGame1.put(1, new Inning(1, 3, 0));
        inningsGame1.put(2, new Inning(2, 1, 3));
        inningsGame1.put(3, new Inning(3, 2, 1));
        inningsGame1.put(4, new Inning(4, 0, 1));
        inningsGame1.put(5, new Inning(5, 2, 2));
        inningsGame1.put(6, new Inning(6, 3, 2));
        inningsGame1.put(7, new Inning(7, 1, 2));

        // This game would be a win (12-11).
        team.addGame(new Game(1, "Apples", "Bananas", inningsGame1));

        LinkedHashMap<Integer, Inning> inningsGame2 = new LinkedHashMap<>();
        inningsGame2.put(1, new Inning(1, 3, 0));
        inningsGame2.put(2, new Inning(2, 1, 3));
        inningsGame2.put(3, new Inning(3, 2, 1));
        inningsGame2.put(4, new Inning(4, 0, 1));
        inningsGame2.put(5, new Inning(5, 2, 2));
        inningsGame2.put(6, new Inning(6, 3, 2));
        inningsGame2.put(7, new Inning(7, 1, 2));

        // This game would be a loss (12-11)
        team.addGame(new Game(1, "Oranges", "Apples", inningsGame2));

        LinkedHashMap<Integer, Inning> inningsGame3 = new LinkedHashMap<>();
        inningsGame3.put(1, new Inning(1, 3, 0));
        inningsGame3.put(2, new Inning(2, 1, 3));
        inningsGame3.put(3, new Inning(3, 2, 1));
        inningsGame3.put(4, new Inning(4, 0, 1));
        inningsGame3.put(5, new Inning(5, 2, 2));
        inningsGame3.put(6, new Inning(6, 3, 2));
        inningsGame3.put(7, new Inning(7, 1, 2));

        // This game would be a win (12-11).
        team.addGame(new Game(1, "Apples", "Pears", inningsGame3));

        double actual = TeamBO.determineStanding(team.getTeamName(), team.getGames());

        assertEquals(0.667, actual);
    }

    @Test
    void testDetermineStanding_2Wins_2Losses() {
        Team team = new Team("Apples");

        // This game would be a win (mercy rule)
        LinkedHashMap<Integer, Inning> game1 = new LinkedHashMap<>();
        game1.put(1, new Inning(1, 4, 0));
        game1.put(2, new Inning(2, 3, 1));
        game1.put(3, new Inning(3, 5, 1));
        game1.put(4, new Inning(4, 2, 2));
        game1.put(5, new Inning(5, 1, 0));
        team.addGame(new Game(1, "Apples", "Bananas", game1));

        // This game would be a win
        LinkedHashMap<Integer, Inning> game2 = new LinkedHashMap<>();
        game2.put(1, new Inning(1, 1, 0));
        game2.put(2, new Inning(2, 0, 2));
        game2.put(3, new Inning(3, 2, 0));
        game2.put(4, new Inning(4, 1, 1));
        game2.put(5, new Inning(5, 0, 0));
        game2.put(6, new Inning(6, 3, 1));
        team.addGame(new Game(2, "Apples", "Pears", game2));

        // This game would be a loss
        LinkedHashMap<Integer, Inning> game3 = new LinkedHashMap<>();
        game3.put(1, new Inning(1, 0, 4));
        game3.put(2, new Inning(2, 1, 3));
        game3.put(3, new Inning(3, 0, 5));
        game3.put(4, new Inning(4, 1, 2));
        game3.put(5, new Inning(5, 0, 1));
        team.addGame(new Game(3, "Apples", "Oranges", game3));

        // This game would be a loss
        LinkedHashMap<Integer, Inning> game4 = new LinkedHashMap<>();
        game4.put(1, new Inning(1, 0, 1));
        game4.put(2, new Inning(2, 1, 0));
        game4.put(3, new Inning(3, 0, 2));
        game4.put(4, new Inning(4, 2, 3));
        game4.put(5, new Inning(5, 1, 1));
        game4.put(6, new Inning(6, 0, 0));
        game4.put(7, new Inning(7, 1, 2));
        team.addGame(new Game(4, "Apples", "Grapes", game4));

        double actual = TeamBO.determineStanding(team.getTeamName(), team.getGames());

        assertEquals(0.500, actual);
    }

    @Test
    void testDetermineStanding2Wins_4Losses() {
        Team team = new Team("Apples");

        // This game would be a win (13-2)
        LinkedHashMap<Integer, Inning> game1 = new LinkedHashMap<>();
        game1.put(1, new Inning(1, 3, 0));
        game1.put(2, new Inning(2, 2, 1));
        game1.put(3, new Inning(3, 4, 0));
        game1.put(4, new Inning(4, 2, 1));
        game1.put(5, new Inning(5, 2, 0));
        team.addGame(new Game(1, "Apples", "Bananas", game1));

        // This game would be a win (8-5)
        LinkedHashMap<Integer, Inning> game2 = new LinkedHashMap<>();
        game2.put(1, new Inning(1, 1, 0));
        game2.put(2, new Inning(2, 2, 1));
        game2.put(3, new Inning(3, 0, 1));
        game2.put(4, new Inning(4, 2, 1));
        game2.put(5, new Inning(5, 1, 1));
        game2.put(6, new Inning(6, 2, 1));
        team.addGame(new Game(2, "Apples", "Oranges", game2));

        // This game would be a loss (1-12)
        LinkedHashMap<Integer, Inning> game3 = new LinkedHashMap<>();
        game3.put(1, new Inning(1, 0, 2));
        game3.put(2, new Inning(2, 1, 3));
        game3.put(3, new Inning(3, 0, 4));
        game3.put(4, new Inning(4, 0, 2));
        game3.put(5, new Inning(5, 0, 1));
        team.addGame(new Game(3, "Apples", "Pears", game3));

        // This game would be a loss (6-8)
        LinkedHashMap<Integer, Inning> game4 = new LinkedHashMap<>();
        game4.put(1, new Inning(1, 1, 2));
        game4.put(2, new Inning(2, 0, 0));
        game4.put(3, new Inning(3, 2, 1));
        game4.put(4, new Inning(4, 1, 2));
        game4.put(5, new Inning(5, 0, 1));
        game4.put(6, new Inning(6, 1, 0));
        game4.put(7, new Inning(7, 1, 2));
        team.addGame(new Game(4, "Apples", "Grapes", game4));

        // This game would be a loss (4-7)
        LinkedHashMap<Integer, Inning> game5 = new LinkedHashMap<>();
        game5.put(1, new Inning(1, 1, 1));
        game5.put(2, new Inning(2, 0, 2));
        game5.put(3, new Inning(3, 2, 1));
        game5.put(4, new Inning(4, 0, 1));
        game5.put(5, new Inning(5, 1, 1));
        game5.put(6, new Inning(6, 0, 1));
        team.addGame(new Game(5, "Apples", "Mangoes", game5));

        // This game would be a loss (5-9)
        LinkedHashMap<Integer, Inning> game6 = new LinkedHashMap<>();
        game6.put(1, new Inning(1, 0, 1));
        game6.put(2, new Inning(2, 1, 2));
        game6.put(3, new Inning(3, 1, 0));
        game6.put(4, new Inning(4, 0, 3));
        game6.put(5, new Inning(5, 2, 1));
        game6.put(6, new Inning(6, 0, 1));
        game6.put(7, new Inning(7, 1, 1));
        team.addGame(new Game(6, "Apples", "Cherries", game6));

        double actual = TeamBO.determineStanding(team.getTeamName(), team.getGames());

        assertEquals(0.333, actual);
    }
}
