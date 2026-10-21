package ca.hccis.softball.controllers;

import ca.hccis.softball.bo.TeamBO;
import ca.hccis.softball.entity.Team;
import ca.hccis.softball.jpa.entity.Game;
import ca.hccis.softball.repositories.GameRepository;
import ca.hccis.softball.util.CisUtility;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Base controller for the general functionality of the application.
 * @author BML
 * @since 20261018
 */
@Controller
public class BaseController {
    private final GameRepository _gr;

    @Autowired
    public BaseController(GameRepository _gr) {
        this._gr = _gr;
    }

    /**
     * Send the user to the home view.
     * @param session The current HTTP session
     * @param model A holder for model attributes
     * @return The home view
     * @author BML
     * @since 20261018
     */
    @RequestMapping("/")
    public String home(HttpSession session, Model model) {
        // TODO DONE Issue #1 Set the current date in the session.
        String today = CisUtility.getCurrentDate("yyyy-MM-dd");
        session.setAttribute("today", today);

        // Get the games.
        ArrayList<Game> games = new ArrayList<>();
        _gr.findAll().forEach(games::add);
        model.addAttribute("games", games);

        ArrayList<Team> teams = TeamBO.selectAllTeams();
        teams.sort(Comparator.comparingDouble(Team::getWinPercentage).reversed());
        model.addAttribute("teams", teams);

        // Return the view.
        return "index";
    }

    /**
     * Send the user to the add game view.
     * @return The add view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/add")
    public String add() {
        return "/league/add";
    }

    /**
     * Send the user to the view games view.
     * @return The view games view
     * @author BML
     * @since 20261020
     */
    @RequestMapping("/view")
    public String view() {
        return "/league/games";
    }
}