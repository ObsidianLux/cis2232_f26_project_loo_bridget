package ca.hccis.softball.controllers;

import ca.hccis.softball.bo.GameBO;
import ca.hccis.softball.entity.ReportHistory;
import ca.hccis.softball.jpa.entity.Game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller to administer reports for the project.
 * @author BML
 * @since 20261018
 */
@Controller
@RequestMapping("/reports")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    /**
     * Send the user to the view that requests the team name for the team history report to generate.
     * @param model A holder for model attributes
     * @return The report input view
     * @author BML
     * @since 20261018
     */
    @RequestMapping("/history")
    public String teamGames(Model model) {
        logger.info("Running the ReportController teamGames() method.");

        ReportHistory report = new ReportHistory();
        logger.debug("reportInput Teams: {}", Arrays.toString(report.getTeams().toArray()));

        model.addAttribute("reportInput", report);

        return "/reports/history";
    }

    /**
     * Send the user to the view that displays the results of the team history report.
     * @param model A holder for model attributes
     * @return The team history report view
     * @author BML
     * @since 20261018
     */
    @RequestMapping("/history/view")
    public String teamGamesResult(Model model, @ModelAttribute ("reportInput") ReportHistory reportHistory) {
        logger.info("Running the ReportController teamGamesResult() method.");

        GameBO gameBO = new GameBO();
        ArrayList<Game> games = gameBO.processSelectAllByTeam(reportHistory.getTeam());
        System.out.println("Games: " + games.size());

        // Display a message if there is no data for the report.
        if (games.isEmpty()) {
            model.addAttribute("message", "No games found for that team.");
            System.out.println("No data found for team: " + reportHistory.getTeam());
        }

        reportHistory.setGames(games);

        // Add the report object to the model so it can be used in the report view.
        model.addAttribute("reportInput", reportHistory);

        return "/reports/historyResults";
    }
}