package ca.hccis.softball.controllers;

import ca.hccis.softball.bo.GameBO;
import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.entity.Team;
import ca.hccis.softball.jpa.entity.Game;
import ca.hccis.softball.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/league")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameRepository _gr;

    public GameController(GameRepository gr) {
        _gr = gr;
    }

    /**
     * Send the user to the view games view.
     * TODO SPRINT 3 - List Functionality
     * @param model The model to store the lists of games in.
     * @return The view games view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/list")
    public String view(Model model) {
        logger.info("Running the GameController view() method.");

        // Get the games (both played and unplayed).
        ArrayList<Game> unplayedGames = new ArrayList<>(_gr.findByGameUnplayed());
        ArrayList<Game> playedGames = new ArrayList<>(_gr.findByGamePlayed());

        // Use the logger to print the arrays of games for debugging.
        logger.debug("Unplayed Games: {}", Arrays.toString(unplayedGames.toArray()));
        logger.debug("Played Games: {}", Arrays.toString(playedGames.toArray()));

        // Add the arrays of games to the model.
        model.addAttribute("unplayedGames", unplayedGames);
        model.addAttribute("playedGames", playedGames);

        // Return the games.html page.
        return "league/games";
    }

    /**
     * Send the user to the add game view.
     * TODO SPRINT 3 - Add Functionality
     * @return The add view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/add")
    public String add(Model model) {
        logger.info("Running the GameController add() method.");

        // TODO Get a list of team names and pass to the view through the model.
        List<String> teamNames = GameBO.selectAllTeams().stream().map(Team::getTeamName).toList();

        model.addAttribute("teamNames", teamNames);

        return "league/add";
    }

    /**
     * Send the user back to the add view with a prompt to either schedule another game or view scheduled games.
     * TODO SPRINT 3 - Add Functionality
     * @return The add view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/add/submit")
    public String addSubmit() {
        logger.info("Running the GameController addSubmit() method.");
        return "league/add";
    }

    /**
     * Send the user to the update game view.
     * TODO SPRINT 3 - Update Functionality
     * @return The update view
     * @author BML
     * @since 20261028
     */
    @RequestMapping("/update")
    public String update(Model model, @RequestParam int id) {
        logger.info("Running the GameController update() method.");
        return "league/add";
    }

    /**
     * Send the user to the list view after updating the game.
     * TODO SPRINT 3 - Update Functionality
     * @return The list view
     * @author BML
     * @since 20261028
     */
    @RequestMapping("/update/submit")
    public String updateSubmit() {
        logger.info("Running the GameController updateSubmit() method.");
        return "league/games";
    }

    /**
     * Send the user back to the list view and included a message about the game being deleted.
     * TODO SPRINT 3 - Delete Functionality
     * @return The games view after deleting the game
     * @author BML
     * @since 20261028
     */
    @RequestMapping("/delete")
    public String delete(Model model, @RequestParam int id) {
        logger.info("Running the GameController delete() method.");
        logger.debug("To delete ID: {}", id);

        // Try to delete the record.
        GameDAO gameDAO = new GameDAO();
        boolean deleted = gameDAO.deleteGame(id);

        // Pass a message to the model to confirm that the game was deleted.
        model.addAttribute("deletedMessage", deleted ? "The game was successfully deleted." : "Something went wrong while attempting to delete the game.");

        // TODO Reload the lists of games if one was deleted.
        if (deleted) {

        }

        return "league/games";
    }
}
