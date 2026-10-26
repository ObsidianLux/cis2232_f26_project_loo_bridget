package ca.hccis.softball.jpa.entity;

import ca.hccis.softball.bo.GameBO;

import ca.hccis.softball.entity.Inning;
import ca.hccis.softball.util.JsonListConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "softballgame", schema = "cis2232_softball_league")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "homeTeam", nullable = false, length = 20)
    private String homeTeam;

    @Size(max = 20)
    @NotNull
    @Column(name = "roadTeam", nullable = false, length = 20)
    private String roadTeam;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "wasForfeited")
    private Boolean wasForfeited;

    @Column(name = "wasMercyWin")
    private Boolean wasMercyWin;

    @Column(name = "homeScore")
    private Integer homeScore;

    @Column(name = "roadScore")
    private Integer roadScore;

    @Column(name = "innings", columnDefinition = "LONGTEXT")
    @Convert(converter = JsonListConverter.class)
    private ArrayList<Inning> innings;

    @NotNull
    @Column(name = "played", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean played;

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

    @Override
    public String toString() {
        return "ID: %d; Home Team: %s; Road Team: %s; Winning Team: %s; Mercy Win: %b; Forfeited: %b; Innings: %s".formatted(id, homeTeam, roadTeam, GameBO.getWinningTeam(this), wasMercyWin, wasForfeited, innings.stream().map(Inning::toString).collect(Collectors.joining(", ")));
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