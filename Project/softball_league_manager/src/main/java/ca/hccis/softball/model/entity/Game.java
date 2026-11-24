package ca.hccis.softball.model.entity;

import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.util.JsonListConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "softballgame", schema = "cis2232_softball_league")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30, message = "The home team's name must not exceed 30 characters.")
    @NotBlank(message = "The home team's name is required.")
    @Column(name = "homeTeam", nullable = false, length = 30)
    private String homeTeam;

    @Size(max = 30, message = "The away team's name must not exceed 30 characters.")
    @NotBlank(message = "The away team's name is required.")
    @Column(name = "awayTeam", nullable = false, length = 30)
    private String awayTeam;

    @NotNull(message = "The scheduled game date is required.")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "wasForfeited")
    private Boolean wasForfeited;

    @Size(max = 30, message = "The team at fault's name must not exceed 30 characters.")
    @Column(name = "teamAtFault")
    private String teamAtFault;

    @Column(name = "innings", columnDefinition = "LONGTEXT")
    @Convert(converter = JsonListConverter.class)
    private List<Inning> innings;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String roadTeam) {
        this.awayTeam = roadTeam;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getWasForfeited() {
        return wasForfeited;
    }

    public void setWasForfeited(Boolean wasForfeited) {
        this.wasForfeited = wasForfeited;
    }

    public String getTeamAtFault() {
        return teamAtFault;
    }

    public void setTeamAtFault(String teamAtFault) {
        this.teamAtFault = teamAtFault;
    }

    public List<Inning> getInnings() {
        return innings;
    }

    public void setInnings(List<Inning> innings) {
        this.innings = innings;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", date=" + date.toString() +
                ", wasForfeited=" + wasForfeited +
                ", teamAtFault='" + teamAtFault + '\'' +
                ", innings=" + (innings == null ? "" : innings.stream().map(Inning::toString).collect(Collectors.joining(", "))) +
                '}';
    }
}