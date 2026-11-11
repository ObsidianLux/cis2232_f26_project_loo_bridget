package ca.hccis.softball.model.entity;

import ca.hccis.softball.model.bo.GameBO;

import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.util.JsonListConverter;

import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Entity
@Table(name = "softballgame", schema = "cis2232_softball_league")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30, message = "Team name must not exceed 30 characters.")
    @NotBlank(message = "Home team name is required.")
    @Column(name = "homeTeam", nullable = false, length = 30)
    private String homeTeam;

    @Size(max = 30, message = "Team name must not exceed 30 characters.")
    @NotBlank(message = "Road team name is required.")
    @Column(name = "roadTeam", nullable = false, length = 30)
    private String roadTeam;

    @NotNull(message = "Scheduled game date is required.")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(message = "It must be clear whether the game has been played or not.")
    @Column(name = "played", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean played = false;

    /* The rest of the attributes are nullable */

    @Column(name = "wasForfeited", columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean wasForfeited = false;

    @Column(name = "wasMercyWin", columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean wasMercyWin = false;

    @Min(value = 0, message = "Home runs cannot be less than 0.")
    @Column(name = "homeScore", columnDefinition = "INT UNSIGNED")
    private Integer homeScore;

    @Min(value = 0, message = "Road runs cannot be less than 0.")
    @Column(name = "roadScore", columnDefinition = "INT UNSIGNED")
    private Integer roadScore;

    @Column(name = "innings", columnDefinition = "LONGTEXT")
    @Convert(converter = JsonListConverter.class)
    private ArrayList<Inning> innings;

    public Game() {}

    public Game(String homeTeam, String roadTeam, LocalDate date, Boolean played) {
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.date = date;
        this.played = played;
    }

    /* VALIDATION */

    /**
     * If the game was played, all fields are required (except innings). Otherwise, only the teams and date are required.
     * @author BML
     * @since 20261107
     */
    @PrePersist
    @PreUpdate
    private void validateConditionalFields() {
        if (Boolean.TRUE.equals(this.played)) {
            if (this.roadScore == null || this.homeScore == null || this.wasMercyWin == null || this.wasForfeited == null) {
                throw new ValidationException("Attributes cannot be null (except innings) when the game has been played.");
            }
        }
    }

    /* GETTERS & SETTERS */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getWasForfeited() {
        return wasForfeited;
    }

    public void setWasForfeited(Boolean forfeited) {
        this.wasForfeited = forfeited;
    }

    public Boolean getWasMercyWin() {
        return wasMercyWin;
    }

    public void setWasMercyWin(Boolean isMercyWin) {
        this.wasMercyWin = isMercyWin;
    }

    public ArrayList<Inning> getInnings() {
        return innings;
    }

    public void setInnings(ArrayList<Inning> innings) {
        this.innings = innings;
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

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getRoadScore() {
        return roadScore;
    }

    public void setRoadScore(Integer roadScore) {
        this.roadScore = roadScore;
    }

    public Boolean getPlayed() {
        return played;
    }

    public void setPlayed(Boolean played) {
        this.played = played;
    }

    /* BO METHODS */

    public String getWinningTeam() {
        return GameBO.getWinningTeam(this);
    }

    public String getPlusMinus(String teamName) {
        return GameBO.getPlusMinus(this, teamName);
    }

    public String getTeamAtFault() {
        return GameBO.getTeamAtFault(this);
    }

    /* OVERRIDES */

    @Override
    public String toString() {
        if (this.played) return "ID: %d; Home Team: %s; Road Team: %s; Date: %s; Winning Team: %s; Mercy Win: %b; Forfeited: %b; Innings: %s; Home Score: %d; Road Score: %d".formatted(id, homeTeam, roadTeam, date.toString(), getWinningTeam(), wasMercyWin, wasForfeited, innings.stream().map(Inning::toString).collect(Collectors.joining(", ")), homeScore, roadScore);
        else return "ID: %d; Home Team: %s; Road Team: %s; Date: %s".formatted(id, homeTeam, roadTeam, date.toString());
    }
}