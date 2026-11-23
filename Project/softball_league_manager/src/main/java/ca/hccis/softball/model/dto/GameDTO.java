package ca.hccis.softball.model.dto;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Separate class for request/response data.
 * This prevents leaking internal fields, avoids issues with serializing entities, and rejects bad input before reaching the database.
 */
// This constraint applies to all games.
@UniqueNames(
        firstName = "homeTeam",
        secondName = "roadTeam"
)
// This constraint applies if the game was forfeited (wasForfeited = true : the checkbox was checked on the form).
@TeamAtFaultRequired(
        wasForfeited = "wasForfeited",
        teamAtFault = "teamAtFault"
)
// This constraint applies if the game was played and not forfeited (wasForfeited = false : the checkbox wasn't checked on the form).
@PlayedGame(
        innings = "innings",
        requiredField = "inningsRequired",
        homeRuns = "homeScore",
        roadRuns = "roadScore"
)
public class GameDTO {
    @Size(max = 30, message = "Team name must not exceed 30 characters.")
    @NotBlank(message = "Home team name is required.")
    private String homeTeam;

    @Size(max = 30, message = "Team name must not exceed 30 characters.")
    @NotBlank(message = "Road team name is required.")
    private String roadTeam;

    @NotNull(message = "Scheduled game date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Integer id;
    private Boolean wasForfeited = false;
    private String teamAtFault;
    private ArrayList<Inning> innings = new ArrayList<>();

    public GameDTO() {}

    public GameDTO(Game game) {
        this.homeTeam = game.getHomeTeam();
        this.roadTeam = game.getRoadTeam();
        this.date = game.getDate();
        this.id = game.getId();

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Innings are required when the game has been played (the date is before today) and wasn't forfeited.
     * This is used by the ValidInnings constraint on this class.
     */
    public boolean isInningsRequired() {
        return date != null && date.isBefore(LocalDate.now()) && !Boolean.TRUE.equals(wasForfeited);
    }

    public Game getMappedGame() {
        // Determine whether the game has happened already or not.
        Boolean played = isInningsRequired();

        // Create the game with basic information.
        Game game = new Game(this.homeTeam, this.roadTeam, this.date, played);

        if (this.id != null) game.setId(this.id);

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
