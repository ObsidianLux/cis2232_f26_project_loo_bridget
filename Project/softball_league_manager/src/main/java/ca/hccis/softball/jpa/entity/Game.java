package ca.hccis.softball.jpa.entity;

import ca.hccis.softball.bo.GameBO;

import ca.hccis.softball.entity.Inning;
import ca.hccis.softball.util.JsonListConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "softballgame", schema = "cis2232_softball_league")
public class Game {
    // Groups that guide validation when creating or updating an object.
    public interface OnCreate {}
    public interface OnUpdate {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Home team name is required.", groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "homeTeam", nullable = false, length = 30)
    private String homeTeam;

    @Size(max = 30, groups = {OnCreate.class, OnUpdate.class})
    @NotBlank(message = "Road team name is required.", groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "roadTeam", nullable = false, length = 30)
    private String roadTeam;

    @NotNull(message = "Scheduled game date is required.", groups = {OnCreate.class, OnUpdate.class})
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(groups = {OnUpdate.class})
    @Column(name = "wasForfeited", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
    private Boolean wasForfeited = false;

    @NotNull(groups = {OnUpdate.class})
    @Column(name = "wasMercyWin", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
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

    @NotNull
    @Column(name = "played", nullable = false, columnDefinition = "BIT(1) DEFAULT FALSE")
    private Boolean played = false;

    @Transient
    private String teamAtFault;

    public Game() {}

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

    public Integer getRoadScore() {
        return roadScore;
    }

    public String getWinningTeam() {
        return GameBO.getWinningTeam(this);
    }

    public String getPlusMinus(String teamName) {
        return GameBO.getPlusMinus(this, teamName);
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
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

    public String getTeamAtFault() {
        return this.teamAtFault;
    }

    public void setTeamAtFault(String teamName) {
        this.teamAtFault = teamName;
    }

    @Override
    public String toString() {
        if (this.played) return "ID: %d; Home Team: %s; Road Team: %s; Date: %s; Winning Team: %s; Mercy Win: %b; Forfeited: %b; Innings: %s; Home Score: %d; Road Score: %d".formatted(id, homeTeam, roadTeam, date.toString(), GameBO.getWinningTeam(this), wasMercyWin, wasForfeited, innings.stream().map(Inning::toString).collect(Collectors.joining(", ")), homeScore, roadScore);
        else return "ID: %d; Home Team: %s; Road Team: %s; Date: %s".formatted(id, homeTeam, roadTeam, date.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game game)) return false;
        return (Objects.equals(getId(), game.getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}