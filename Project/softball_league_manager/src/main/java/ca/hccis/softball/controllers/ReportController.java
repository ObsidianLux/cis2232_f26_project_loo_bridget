package ca.hccis.softball.controllers;

import ca.hccis.softball.bo.GameBO;
import ca.hccis.softball.dao.GameDAO;
import ca.hccis.softball.entity.ReportHistory;
import ca.hccis.softball.entity.Team;
import ca.hccis.softball.jpa.entity.Game;

import ca.hccis.softball.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Arrays;

/**
 * Controller to administer reports for the project.
 * Sprint 2 - Work Issue 5
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

        model.addAttribute("reportHistory", report);

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
    public String teamGamesResult(Model model, @ModelAttribute ("reportHistory") ReportHistory reportHistory) throws IOException {
        logger.info("Running the ReportController teamGamesResult() method.");

        reportHistory.setTeam(reportHistory.getTeamByName());

        // Display a message if there is no data for the report.
        if (reportHistory.getTeam().getNumberOfGamesPlayed() == 0) {
            model.addAttribute("message", "This team has not played any games.");
            System.out.println("No data found for team: " + reportHistory.getTeamName());
        } else {
            // Write the report to a file.
            Utility.writeGamesToFile("teamHistoryReport", reportHistory.getTeam().getPlayedGames());
        }

        // Add the report object to the model so it can be used in the report view.
        model.addAttribute("reportHistory", reportHistory);

        return "/reports/history";
    }
}