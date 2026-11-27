package ca.hccis.softball.controller;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.dto.GameRequest;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.service.GameService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService service;

    @Autowired
    public GameController(GameService service) {
        this.service = service;
    }

    // This attribute is always added to the model anytime something routes through this controller.
    @ModelAttribute
    public void addGameAttributes(Model model) {
        // Get a list of team names and pass to the view through the model.
        model.addAttribute("teamNames", GameBO.getAllUniqueTeamNames());
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

        // Add a new GameRequest object to the model to store form data.
        model.addAttribute("game", new GameRequest());

        return "games/add";
    }

    /**
     * Send the user back to the add view.
     * TODO SPRINT 3 - Add Functionality
     * @return The add view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/add/submit")
    public String addSubmit(Model model, @Valid @ModelAttribute("game") GameRequest game, BindingResult bindingResult) {
        logger.info("Running the GameController addSubmit() method.");

        // Print errors to console for debugging.
        logger.info(bindingResult.toString());

        if (!bindingResult.hasErrors()) {
            // Save the mapped entity.
            service.insertGame(game.toEntity());

            logger.info("Added new game to repository.");

            // Add a new GameDTO object to the model to store form data.
            model.addAttribute("game", new GameRequest());

            // Add a message to the model to tell the user that their game was added to the database.
            model.addAttribute("successMessage", "%s vs. %s game added successfully.".formatted(game.homeTeamName(), game.awayTeamName()));
        }

        return "games/add";
    }

    /**
     * Send the user to the update game view.
     * TODO SPRINT 3 - Update Functionality
     * @return The update view
     * @author BML
     * @since 20261028
     */
    @RequestMapping("/update/{id}")
    public String update(Model model, @PathVariable int id) {
        logger.info("Running the GameController update() method.");

        // Get the existing game from the database.
        GameRequest game = service.getGameById(id);

        // Convert to GameRequest and add to model.
        model.addAttribute("game", game);

        // For debugging.
        logger.info(game.toString());

        return "games/add";
    }

    /**
     * Send the user to the list view after updating the game.
     * TODO SPRINT 3 - Update Functionality
     * @return The list view
     * @author BML
     * @since 20261111
     */
    @RequestMapping("/update/{id}/submit")
    public String updateSubmit(@PathVariable int id, @Valid @ModelAttribute("game") GameRequest game, BindingResult bindingResult) {
        logger.info("Running the GameController updateSubmit() method.");

        // Print errors to console for debugging.
        logger.debug(bindingResult.toString());

        if (!bindingResult.hasErrors()) {
            // Save the mapped entity.
            service.updateGame(id, game);

            logger.info("Updated game with id: {}", id);

            // Return to the index page. Using redirect should get the BaseController's @ModelAttribute method to run automatically.
            return "redirect:/";
        }

        return "games/add";
    }
}
