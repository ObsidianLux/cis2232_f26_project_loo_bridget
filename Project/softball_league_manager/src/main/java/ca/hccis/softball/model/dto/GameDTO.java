package ca.hccis.softball.model.dto;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;

// TODO Add validation to make sure that all games have at least 7 innings unless they haven't been played, the mercy rule applied, or they were forfeited.

@ConditionalRequired(
        conditionalField = "wasForfeited",
        dependentField = "teamAtFault",
        message = "The team who forfeited the game is required."
)
@MercyDifferential(
        listField = "innings",
        homeRuns = "homeRuns",
        roadRuns = "roadRuns"
)
@NoTie(
        firstScore = "homeScore",
        secondScore = "roadScore",
        listField = "innings"
)
@MinInnings(
        innings = "innings",
        requiredField = "inningsRequired",
        message = "The game must have at least 5 valid innings."
)
@UniqueNames(
        firstName = "homeTeam",
        secondName = "roadTeam"
)
public class GameDTO {
    @Size(max = 30, message = "Team name must not exceed 30 characters.")
    @NotBlank(message = "Home team name is required.")
    private String homeTeam;

    @Size(max = 30, message = "Team name must not exceed 30 characters.")
    @NotBlank(message = "Road team name is required.")
    private String roadTeam;

    @NotNull(message = "Scheduled game date is required.")
    private LocalDate date;

    private Boolean wasForfeited = false;
    private String teamAtFault;
    private ArrayList<Inning> innings = new ArrayList<>();

    public GameDTO() {}

    public GameDTO(Game game) {
        this.homeTeam = game.getHomeTeam();
        this.roadTeam = game.getRoadTeam();
        this.date = game.getDate();

        if (game.getPlayed()) {
            this.wasForfeited = game.getWasForfeited();
            this.innings = game.getInnings();
            this.teamAtFault =  game.getTeamAtFault();
        } else {
            this.wasForfeited = false;
            this.innings = new ArrayList<>();
            this.teamAtFault =  "";
        }
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(String roadTeam) {
        this.roadTeam = roadTeam;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTeamAtFault() {
        return teamAtFault;
    }

    public void setTeamAtFault(String teamAtFault) {
        this.teamAtFault = teamAtFault;
    }

    public ArrayList<Inning> getInnings() {
        return innings;
    }

    public void setInnings(ArrayList<Inning> innings) {
        this.innings = innings;
    }

    public Boolean getWasForfeited() {
        return wasForfeited;
    }

    public void setWasForfeited(Boolean wasForfeited) {
        this.wasForfeited = wasForfeited;
    }

    public int getHomeScore() {
        return innings.stream().mapToInt(Inning::getHomeRuns).sum();
    }

    public int getRoadScore() {
        return innings.stream().mapToInt(Inning::getRoadRuns).sum();
    }

    /**
     * Innings are required when the game has been played (the date is before today) and wasn't forfeited.
     */
    public boolean areInningsRequired() {
        return date != null && date.isBefore(LocalDate.now()) && !Boolean.TRUE.equals(wasForfeited);
    }

    public Game getMappedGame() {
        // Determine whether the game has happened already or not.
        Boolean played = areInningsRequired();

        // Create the game with basic information.
        Game game = new Game(this.homeTeam, this.roadTeam, this.date, played);

        // If not played, return the game. Otherwise, set the rest of the attributes.
        if (played) {
            game.setWasForfeited(this.wasForfeited);

            if (this.wasForfeited) {
                // If forfeited, set the scores based on the team at fault and empty the innings list.
                return GameBO.setPlayForfeit(game, this.teamAtFault);
            } else {
                // The game wasn't forfeited, so process the innings list to get the final scores and determine whether the game ended due to the Mercy Rule.
                ArrayList<Inning> innings = new ArrayList<>();

                for (int index = 0; index < this.innings.size(); index++) {
                    innings.add(setInningNumber(this.innings.get(index), index));
                }

                game.setInnings(innings);
                return GameBO.setPlayNotForfeit(game);
            }
        }

        return game;
    }

    /**
     * Adds an inning number to an Inning.
     * @param inning Inning with home runs and road runs
     * @param index Index of inning in parent ArrayList
     * @return Inning
     * @author BML
     * @since 20261111
     */
    private Inning setInningNumber(Inning inning, int index) {
        inning.setInningNumber(index + 1);
        return inning;
    }
}
