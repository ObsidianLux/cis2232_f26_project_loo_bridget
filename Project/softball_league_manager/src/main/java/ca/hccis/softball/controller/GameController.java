package ca.hccis.softball.controller;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.dto.GameDTO;
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
    @GetMapping("/add")
    public String add(Model model) {
        logger.info("Running the GameController add() method.");

        // Add a new GameDTO object to the model to store form data.
        model.addAttribute("game", new GameDTO());

        // Route to provide to the form for the action attribute.
        model.addAttribute("action", "/game/add/submit");

        // Add something to help thymeleaf determine whether the form should be 'add' or update.
        model.addAttribute("isAdd", true);

        return "games/add";
    }

    /**
     * Send the user back to the add view.
     * TODO SPRINT 3 - Add Functionality
     * @return The add view
     * @author BML
     * @since 20261020
     */
    @PostMapping("/add/submit")
    public String addSubmit(Model model, @Valid @ModelAttribute("game") GameDTO game, BindingResult bindingResult) {
        logger.info("Running the GameController addSubmit() method.");

        // Print errors to console for debugging.
        logger.info(bindingResult.toString());

        // Route to provide to the form for the action attribute.
        model.addAttribute("action", "/game/add/submit");

        // Add something to help thymeleaf determine whether the form should be 'add' or update.
        model.addAttribute("isAdd", true);

        if (!bindingResult.hasErrors()) {
            // Save the mapped entity.
            service.insertGame(game.getMappedGame());

            logger.info("Added new game to repository.");

            // Add a new GameDTO object to the model to store form data.
            model.addAttribute("game", new GameDTO());

            // TODO Add some kind of notification to tell the user that their game was added to the database.
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
    @GetMapping("/update")
    public String update(Model model, @RequestParam int id) {
        logger.info("Running the GameController update() method.");

        // Get the existing game from the database.
        Game game = service.getGameById(id);

        // Convert to GameDTO and add to model.
        model.addAttribute("game", new GameDTO(game));

        // Route to provide to the form for the action attribute.
        model.addAttribute("action", "/game/update/submit(id=${" + id + "})");

        // Add something to help thymeleaf determine whether the form should be 'add' or update.
        model.addAttribute("isAdd", false);

        return "games/update";
    }

    /**
     * Send the user to the list view after updating the game.
     * TODO SPRINT 3 - Update Functionality
     * @return The list view
     * @author BML
     * @since 20261111
     */
    @PostMapping("/update/submit")
    public String updateSubmit(Model model, @RequestParam int id, @Valid @ModelAttribute("game") GameDTO game, BindingResult bindingResult) {
        logger.info("Running the GameController updateSubmit() method.");

        // Print errors to console for debugging.
        logger.debug(bindingResult.toString());

        // Route to provide to the form for the action attribute.
        model.addAttribute("action", "/game/update/submit(id=${" + id + "})");

        // Add something to help thymeleaf determine whether the form should be 'add' or update.
        model.addAttribute("isAdd", false);

        if (!bindingResult.hasErrors()) {
            // Save the mapped entity.
            service.updateGame(id, game);

            logger.info("Updated game with id: {}", id);

            // Return to the index page. Using redirect should get the BaseController's @ModelAttribute method to run automatically.
            return "redirect:/";
        }

        return "games/update";
    }
}
