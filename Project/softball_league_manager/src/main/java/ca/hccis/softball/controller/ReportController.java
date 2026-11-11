package ca.hccis.softball.controller;

import ca.hccis.softball.model.domain.ReportHistory;

import ca.hccis.softball.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * Sprint 2
     * @param model A holder for model attributes
     * @return The report input view
     * @author BML
     * @since 20261018
     * @modified BML 20261105 Changed the name of method and report in logger and added comments.
     */
    @RequestMapping("/history")
    public String teamHistoryReport(Model model) {
        logger.info("Running the ReportController teamHistoryReport() method.");

        // Create a new ReportHistory object and display the list of teams in the logger for debugging.
        ReportHistory report = new ReportHistory();
        logger.debug("reportHistory Teams: {}", Arrays.toString(report.getTeams().toArray()));

        // Add the report to the model as reportHistory.
        model.addAttribute("reportHistory", report);

        // Add a message to the model to indicate that no teams have been selected yet.
        model.addAttribute("message", "No team selected.");

        // Display the history.html page.
        return "reports/history";
    }

    /**
     * Update the history.html page with the results of the team history report.
     * Sprint 2
     * @param model A holder for model attributes
     * @param reportHistory The custom object to store report data
     * @return The team history report view
     * @author BML
     * @since 20261018
     * @modified BML 20261105 Changed method name and updated the method to work when receiving data from either the model or the request parameters.
     */
    @RequestMapping("/history/view")
    public String teamHistoryReportResults(Model model, @ModelAttribute("reportHistory") ReportHistory reportHistory) throws IOException {
        logger.info("Running the ReportController teamHistoryReportResults() method.");

        /*
        The reportHistory object should automatically be created when this method is run if it doesn't already exist.
        If the call came from index.html, the teamName request parameter should automatically be assigned to the reportHistory teamName attribute.
        */

        // Add all teams so that the user can select a different team.
        logger.debug("reportInput Teams: {}", Arrays.toString(reportHistory.getTeams().toArray()));

        // Set the Team object using its name.
        reportHistory.setTeam(reportHistory.getTeamByName());

        // Display a message in the console if there is no data for the report.
        if (reportHistory.getTeam().getNumberOfGamesPlayed() == 0) {
            System.out.println("No data found for team: " + reportHistory.getTeamName());
            // Add a message to the model to indicate that the teams hasn't played any games yet.
            model.addAttribute("message", "This team has not played any games yet!");
        } else {
            // Write the report to a file.
            Utility.writeGamesToFile("teamHistoryReport", reportHistory.getTeam().getPlayedGames());
        }

        // Add the report object back to the model so it can be used in the report view.
        model.addAttribute("reportHistory", reportHistory);

        return "reports/history";
    }
}