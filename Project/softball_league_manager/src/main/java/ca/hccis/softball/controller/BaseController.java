package ca.hccis.softball.controller;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.service.GameService;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Base controller for the general functionality of the application.
 * @author BML
 * @since 20261018
 */
@Controller
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private final GameService service;

    @Autowired
    public BaseController(GameService service) {
        this.service = service;
    }

    @ModelAttribute
    public void addIndexAttributes(Model model) {
        // Get the upcoming and past games and add them to the model.
        model.addAttribute("playedGames", service.getPlayedGames());
        model.addAttribute("upcomingGames", service.getUnplayedGames());

        // Get a sorted list of teams and add it to the model.
        model.addAttribute("teams", GameBO.selectAllTeamsSortedByPCT());
    }

    /**
     * Send the user to the home view.
     * TODO SPRINT 3 - List Functionality
     * @param session The current HTTP session
     * @return The home view
     * @author BML
     * @since 20261018
     */
    @RequestMapping("/")
    public String home(HttpSession session) {
        // TODO DONE Sprint 1 - Issue #1 Set the current date in the session.
        session.setAttribute("today", LocalDate.now());

        // Return the view.
        return "index";
    }

    /**
     * Send the user back to the index view.
     * TODO SPRINT 3 - Delete Functionality
     * @return The index view after deleting the game
     * @author BML
     * @since 20261028
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        logger.info("Running the GameController delete() method.");
        logger.debug("ID of game to delete: {}", id);

        // Try to delete the record.
        service.deleteGame(id);

        return "index";
    }
}

