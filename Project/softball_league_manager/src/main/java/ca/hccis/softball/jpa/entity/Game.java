package ca.hccis.softball.jpa.entity;

import com.google.gson.Gson;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

@Entity
@Table(name = "softballgame", schema = "cis2232_softball_league")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "forfeited", nullable = false)
    private Boolean forfeited;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isMercyWin", nullable = false)
    private Boolean isMercyWin;

    @NotNull
    @Column(name = "scoreDifferential", nullable = false)
    private Integer scoreDifferential;

    @Lob
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "innings", columnDefinition = "LONGTEXT")
    private String innings;

    @Size(max = 20)
    @NotNull
    @Column(name = "homeTeam", nullable = false, length = 20)
    private String homeTeam;

    @Size(max = 20)
    @NotNull
    @Column(name = "roadTeam", nullable = false, length = 20)
    private String roadTeam;

    @Size(max = 20)
    @NotNull
    @Column(name = "winningTeam", nullable = false, length = 20)
    private String winningTeam;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getForfeited() {
        return forfeited;
    }

    public void setForfeited(Boolean forfeited) {
        this.forfeited = forfeited;
    }

    public Boolean getIsMercyWin() {
        return isMercyWin;
    }

    public void setIsMercyWin(Boolean isMercyWin) {
        this.isMercyWin = isMercyWin;
    }

    public Integer getScoreDifferential() {
        return scoreDifferential;
    }

    public void setScoreDifferential(Integer scoreDifferential) {
        this.scoreDifferential = scoreDifferential;
    }

    public String getInnings() {
        return innings;
    }

    public void setInnings(String innings) {
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

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    /**
     * Use Gson to serialize this instance in JSON format.
     * @return JSON-formatted string.
     * @author BML
     * @since 20261010
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "ID: %d; Home Team: %s; Road Team: %s; Winning Team: %s; Mercy Win: %b; Forfeited: %b; Score Differential: %d; Innings: %s".formatted(id, homeTeam, roadTeam, winningTeam, isMercyWin, forfeited, scoreDifferential, innings);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Game game)) return false;
        return (Objects.equals(getHomeTeam(), game.getHomeTeam()) && Objects.equals(getRoadTeam(), game.getRoadTeam())) || (Objects.equals(getHomeTeam(), game.getRoadTeam()) && Objects.equals(getRoadTeam(), game.getHomeTeam()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHomeTeam(), getRoadTeam());
    }
}