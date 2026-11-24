package ca.hccis.softball.dao;

import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.model.entity.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * DAO class to access the database.
 * @author BML
 * @since 20261016
 */
public class GameDAO {
    private final String connectionString;
    private final String userName;
    private final String password;

    private static final Logger logger = LoggerFactory.getLogger(GameDAO.class);

    public GameDAO() {
        // Get the application properties file.
        String propertyFileName = "application";
        ResourceBundle rb = ResourceBundle.getBundle(propertyFileName);

        // Get the database connection information.
        connectionString = rb.getString("spring.datasource.url");
        userName = rb.getString("spring.datasource.username");
        password = rb.getString("spring.datasource.password");
    }

    /**
     * Select all games.
     * TODO SPRINT 3 - List Functionality.
     * @return A list of all games.
     * @author BML
     * @since 20261016
     */
    public List<Game> selectAll() {
        try (
                Connection conn = DriverManager.getConnection(connectionString, userName, password);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM SoftballGame")
        ) {
            return loadList(rs);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            // Returns an empty ArrayList if no games were retrieved.
            return new ArrayList<>();
        }
    }

    /**
     * Select all games that were played by the given team.
     * @param teamName The team name to filter by.
     * @return A list of games.
     * @author BML
     * @since 20261016
     */
    public List<Game> selectAllByTeamName(String teamName) {
        String query = "SELECT * FROM SoftballGame WHERE homeTeam = ? OR awayTeam = ?;";

        try (
                Connection conn = DriverManager.getConnection(connectionString, userName, password);
                PreparedStatement stmt = conn.prepareStatement(query)
                ) {
            stmt.setString(1, teamName);
            stmt.setString(2, teamName);
            try (ResultSet rs = stmt.executeQuery()) {
                return loadList(rs);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            // Returns an empty ArrayList if no games were retrieved.
            return new ArrayList<>();
        }
    }

    /**
     * Loops through the given ResultSet and converts each record into a Game object.
     * @param rs The result set
     * @return An ArrayList of games
     * @throws SQLException Throws if there was an issue with the SQL connection.
     * @author BML
     * @since 20261016
     */
    private List<Game> loadList(ResultSet rs) throws SQLException {
        List<Game> games = new ArrayList<>();

        Gson gson = new Gson();
        Type inningListType = new TypeToken<List<Inning>>() {}.getType();

        // Loop through the result set to convert each record.
        while (rs.next()) {
            Game game = new Game();

            game.setId(rs.getInt("id"));
            game.setHomeTeam(rs.getString("homeTeam"));
            game.setAwayTeam(rs.getString("awayTeam"));
            game.setDate(rs.getDate("date").toLocalDate());
            game.setWasForfeited(rs.getBoolean("wasForfeited"));
            game.setTeamAtFault(rs.getString("teamAtFault"));

            // Extract raw JSON string from the database.
            String jsonInnings = rs.getString("innings");

            // Convert the JSON string to List<Inning> using Gson.
            List<Inning> inningsList;
            if (jsonInnings != null && !jsonInnings.isBlank()) {
                inningsList = gson.fromJson(jsonInnings, inningListType);
            } else {
                // If the JSON string is empty, set to null.
                inningsList = null;
            }

            game.setInnings(inningsList);

            games.add(game);
        }

        // Returns an empty List if no games were retrieved.
        return games;
    }
}
