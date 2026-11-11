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
    public ArrayList<Game> selectAll() {
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
    public ArrayList<Game> selectAllByTeamName(String teamName) {
        String query = "SELECT * FROM SoftballGame WHERE homeTeam = ? OR roadTeam = ?;";

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

    /**
     * Insert a new game record into the SoftballGame table.
     * TODO SPRINT 3 - Add Functionality
     * @param game The game to add.
     * @return True if successfully inserted, otherwise false.
     * @author BML
     * @since 20261028
     */
    public boolean insertGame(Game game) {
        String query = "INSERT INTO SoftballGame (homeTeam, roadTeam, date, wasForfeited, wasMercyWin, homeScore, roadScore, innings, played) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (
                Connection conn = DriverManager.getConnection(connectionString, userName, password);
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, game.getHomeTeam());
            stmt.setString(2, game.getRoadTeam());
            stmt.setObject(3, game.getDate());
            stmt.setBoolean(4, game.getWasForfeited());
            stmt.setBoolean(5, game.getWasMercyWin());
            stmt.setInt(6, game.getHomeScore());
            stmt.setInt(7, game.getRoadScore());
            stmt.setObject(8, game.getInnings());
            try (ResultSet rs = stmt.executeQuery()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            // Couldn't insert the record.
            return false;
        }
    }

    /**
     * Update an existing game record in the SoftballGame table.
     * TODO SPRINT 3 - Update Functionality
     * @param game The game to update with the updated data.
     * @return True if successfully updated, otherwise false.
     * @author BML
     * @since 20261028
     */
    public boolean updateGame(Game game) {
        String query = "UPDATE SoftballGame SET homeTeam = ?, roadTeam = ?, date = ?, wasForfeited = ?, wasMercyWin = ?, homeScore = ?, roadScore = ?, innings = ?, played = ? WHERE id = ?;";

        try (
                Connection conn = DriverManager.getConnection(connectionString, userName, password);
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, game.getHomeTeam());
            stmt.setString(2, game.getRoadTeam());
            stmt.setObject(3, game.getDate());
            stmt.setBoolean(4, game.getWasForfeited());
            stmt.setBoolean(5, game.getWasMercyWin());
            stmt.setInt(6, game.getHomeScore());
            stmt.setInt(7, game.getRoadScore());
            stmt.setObject(8, game.getInnings());
            stmt.setBoolean(9, game.getPlayed());
            stmt.setInt(10, game.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            // Couldn't update the record.
            return false;
        }
    }

    /**
     * Delete an existing game record from the SoftballGame table using its ID.
     * TODO SPRINT 3 - Delete Functionality
     * @param gameID The ID of the game to delete.
     * @return True if successfully delete, otherwise false.
     * @author BML
     * @since 20261028
     */
    public boolean deleteGame(int gameID) {
        String query = "DELETE FROM SoftballGame WHERE id = ?;";

        try (
                Connection conn = DriverManager.getConnection(connectionString, userName, password);
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(1, gameID);
            try (ResultSet rs = stmt.executeQuery()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            // Couldn't delete the record.
            return false;
        }
    }
}
