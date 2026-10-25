package ca.hccis.softball.dao;

import ca.hccis.softball.entity.Inning;
import ca.hccis.softball.jpa.entity.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
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

        Gson gson = new Gson();
        Type inningListType = new TypeToken<ArrayList<Inning>>() {}.getType();

        // Loop through the result set to convert each record.
        while (rs.next()) {
            Game game = new Game();

            game.setId(rs.getInt("id"));
            game.setHomeTeam(rs.getString("homeTeam"));
            game.setRoadTeam(rs.getString("roadTeam"));
            game.setDate(rs.getDate("date").toLocalDate());
            game.setHomeScore(rs.getInt("homeScore"));
            game.setRoadScore(rs.getInt("roadScore"));
            game.setWasForfeited(rs.getBoolean("wasForfeited"));
            game.setWasMercyWin(rs.getBoolean("wasMercyWin"));
            game.setPlayed(rs.getBoolean("played"));

            // Extract raw JSON string from the database.
            String jsonInnings = rs.getString("innings");

            // Convert the JSON string to ArrayList<Inning> using Gson.
            ArrayList<Inning> inningsList;
            if (jsonInnings != null && !jsonInnings.isBlank()) {
                inningsList = gson.fromJson(jsonInnings, inningListType);
            } else {
                // If the JSON string is empty, set to empty ArrayList.
                inningsList = new ArrayList<>();
            }

            game.setInnings(inningsList);

            games.add(game);
        }

        // Returns an empty ArrayList if no games were retrieved.
        return games;
    }

    public Game getGameById(int id) {
        Game game = null;

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SoftballGame WHERE id = ?;")) {
            try {
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                game = loadList(rs).get(0);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        // Returns null if no game was retrieved.
        return game;
    }
}
