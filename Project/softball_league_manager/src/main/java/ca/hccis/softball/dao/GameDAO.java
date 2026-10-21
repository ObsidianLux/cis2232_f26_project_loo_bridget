package ca.hccis.softball.dao;

import ca.hccis.softball.jpa.entity.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * DAO class to access the database.
 * @author BML
 * @since 20261016
 */
public class GameDAO {
    private static ResultSet rs;
    private Connection conn = null;
    private static final Logger logger = LoggerFactory.getLogger(GameDAO.class);

    public GameDAO() {
        // Get the application properties file.
        String propertyFileName = "application";
        ResourceBundle rb = ResourceBundle.getBundle(propertyFileName);

        // Get the database connection information.
        String connectionString = rb.getString("spring.datasource.url");
        String userName = rb.getString("spring.datasource.username");
        String password = rb.getString("spring.datasource.password");

        // Connect to the database.
        try {
            // Use DriverManager to get a connection to the MySQL database.
            // Adding the Java connector to MySQL in the application's dependencies enables this connection.
            conn = DriverManager.getConnection(connectionString, userName, password);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Select all games.
     * @return A list of all games.
     * @author BML
     * @since 20261016
     */
    public ArrayList<Game> selectAll() {
        ArrayList<Game> games = new ArrayList<>();

        // Perform try with conn resource. Doing it this way should automatically close the statement upon exiting the try block.
        try (Statement stmt = conn.createStatement()) {
            try {
                rs = stmt.executeQuery("SELECT * FROM SoftballGame");
                games = loadList(rs);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        // Returns an empty ArrayList if no games were retrieved.
        return games;
    }

    /**
     * Select all games that were played by the given team.
     * @param teamName The team name to filter by.
     * @return A list of games.
     * @author BML
     * @since 20261016
     */
    public ArrayList<Game> selectAllByTeamName(String teamName) {
        ArrayList<Game> games = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SoftballGame WHERE homeTeam = ? OR roadTeam = ?;")) {
            try {
                stmt.setString(1, teamName);
                stmt.setString(2, teamName);

                rs = stmt.executeQuery();
                games = loadList(rs);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        // Returns an empty ArrayList if no games were retrieved.
        return games;
    }

    /**
     * Loops through the given ResultSet and converts each record into a Game object.
     * @param rs The result set
     * @return An ArrayList of games
     * @throws SQLException Throws if there was an issue with the SQL connection.
     * @author BML
     * @since 20261016
     */
    private ArrayList<Game> loadList(ResultSet rs) throws SQLException {
        ArrayList<Game> games = new ArrayList<>();

        // Loop through the result set to convert each record.
        while (rs.next()) {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setForfeited(rs.getBoolean("forfeited"));
            game.setIsMercyWin(rs.getBoolean("isMercyWin"));
            game.setScoreDifferential(rs.getInt("scoreDifferential"));

            game.setHomeTeam(rs.getString("homeTeam"));
            game.setRoadTeam(rs.getString("roadTeam"));
            game.setWinningTeam(rs.getString("winningTeam"));
            game.setInnings(rs.getString("innings"));

            games.add(game);
        }

        // Returns an empty ArrayList if no games were retrieved.
        return games;
    }
}
