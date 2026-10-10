package ca.hccis.softball.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

/**
 * Controller to administer reports for the project.
 * @author BML
 * @since 20261009
 */
@Controller
@RequestMapping("/report")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    /**
     * Sends the user to the view with the list of available reports.
     * @param model A holder for model attributes
     * @param session The current HTTP session
     * @return The Home view
     * @author BML
     * @since 20261009
     */
    @RequestMapping("")
    public String home(Model model, HttpSession session) {
        // TODO Issue #1: Set the current date in the session.
        logger.info("Running the reports controller base method.");
        return "report/menu";
    }

    /**
     * Sends the user to the view with the standings of all tracked teams.
     * @param model A holder for model attributes
     * @return The standings report view
     * @author BML
     * @since 20261009
     */
    @RequestMapping("/standings")
    public String reportStandings(Model model) {
        logger.info("Running the ReportController reportStandings() method.");

        // TODO: Interact with the TeamDAO class.

        // TODO: Write model code to get the appropriate information from the DB.

        // TODO: Add the data to a collection in the ReportStandings class.

        // TODO: Add a message in case the report does not contain any data.

        // TODO DONE: Add the object to the model so it can be used in the view.
        model.addAttribute("reportInput", reportStandings);

        // Send the user to the Standings report view.
        return "report/reportStandings";
    }

    /**
     * Send the user to the view that requests the team name for the team history report to generate.
     * @param model A holder for model attributes
     * @return The report input view
     * @author BML
     * @since 20261009
     */
    @RequestMapping("/team/history")
    public String reportTeamHistory(Model model) {
        logger.info("Running the ReportController reportTeamHistory() method.");

        // Add the provided team name to the model attributes.
        model.addAttribute("reportInput", new ReportHistory());
        return "report/reportTeamHistory";
    }

    /**
     * Send the user to the view that
     * @param model A holder for model attributes
     * @param reportHistory A team history report object
     * @return The team history report view
     * @author BML
     * @since 20261009
     */
    @RequestMapping("report/team/history/submit")
    public String reportTeamHistorySubmit(Model model, @ModelAttribute("reportInput") ReportHistory reportHistory) {

    }
}
