package ca.hccis.softball.controllers;

import ca.hccis.softball.bo.GameBO;
import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.entity.GameDTO;
import ca.hccis.softball.entity.Inning;
import ca.hccis.softball.entity.Team;
import ca.hccis.softball.jpa.entity.Game;
import ca.hccis.softball.repositories.GameRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Send the user to the initial add game view. This view asks the user for the teams and date of the game to add.
     * TODO SPRINT 3 - Add Functionality
     * @return The add view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/add")
    public String add(Model model) {
        logger.info("Running the GameController add() method.");

        // Get a list of team names and pass to the view through the model.
        List<String> teamNames = GameBO.selectAllTeams().stream().map(Team::getTeamName).toList();
        model.addAttribute("teamNames", teamNames);

        // Add a GameDTO object to the model.
        model.addAttribute("game", getNewGameDTO());

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
    public String addSubmit(Model model, @Valid @ModelAttribute GameDTO game, BindingResult bindingResult) {
        logger.info("Running the GameController addSubmit() method.");

        // Check DTO validation rules first. If there are errors, return to form view with error messages.
        if (!bindingResult.hasErrors()) {
            // Save the mapped entity.
            _gr.save(game.getMappedGame());

            // Add a success message to display to the user.
            model.addAttribute("success", "The game was successfully scheduled!");

            // Add a new GameDTO to the model if the user wants to add another game.
            model.addAttribute("game", getNewGameDTO());
        }

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

    /**
     * Create a new GameDTO object to pass to the model.
     * @return New GameDTO
     * @author BML
     * @since 20261109
     */
    private GameDTO getNewGameDTO() {
        // Prepare 7 empty innings to pass in for numbering on the form.
        GameDTO gameDTO = new GameDTO();
        ArrayList<Inning> innings = new ArrayList<>();

        for (int index = 1; index <= 7; index++) innings.add(new Inning(index));

        gameDTO.setInnings(innings);

        return gameDTO;
    }
}
