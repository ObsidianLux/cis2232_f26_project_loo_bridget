package ca.hccis.softball.entity;

import ca.hccis.softball.jpa.entity.Game;
import ca.hccis.softball.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;

@ConditionalRequired(
        conditionalField = "wasForfeited",
        dependentField = "teamAtFault",
        message = "The team who forfeited the game is required."
)
@ConditionalRequired(
        conditionalField = "wasForfeited",
        expectedValue = false,
        dependentField = "innings",
        message = "The game must have at least 5 valid innings."
)
@MercyDifferential(
        listField = "innings",
        homeRuns = "homeRuns",
        roadRuns = "roadRuns"
)
// TODO Fix no tie constraint.
//@NoTie(
//        firstScore = "homeScore",
//        secondScore = "roadScore"
//)
@MinInnings(
        innings = "innings"
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

    public GameDTO() {

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

    public Game getMappedGame() {
        // Determine whether the game has happened already or not.
        Boolean played = this.date.isBefore(LocalDate.now());

        // Create the game with basic information.
        Game game = new Game(this.homeTeam, this.roadTeam, this.date, played);

        // If not played, return the game. Otherwise, set the rest of the attributes.
        if (played) {
            game.setWasForfeited(this.wasForfeited);

            if (this.wasForfeited) {
                // If forfeited, set the scores based on the team at fault and empty the innings list.
                game.setWasMercyWin(false);
                game.setInnings(new ArrayList<>());

                if (this.teamAtFault.equals(this.homeTeam)) {
                    game.setHomeScore(0);
                    game.setRoadScore(7);
                } else {
                    game.setHomeScore(7);
                    game.setRoadScore(0);
                }
            } else {
                // The game wasn't forfeited, so process the innings list to get the final scores and determine whether the game ended due to the Mercy Rule.
                game.setInnings(this.innings);
                game.setWasMercyWin(this.innings.size() == 5);

                int homeRuns = 0;
                int roadRuns = 0;

                for (Inning inning : this.innings) {
                    homeRuns += inning.getHomeRuns();
                    roadRuns += inning.getRoadRuns();
                }

                game.setHomeScore(homeRuns);
                game.setRoadScore(roadRuns);
            }

            return game;
        } else return game;
    }
}
