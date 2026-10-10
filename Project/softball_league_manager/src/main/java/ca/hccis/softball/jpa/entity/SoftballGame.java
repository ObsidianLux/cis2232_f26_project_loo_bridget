package ca.hccis.softball.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "softballgame", schema = "cis2232_softball_league")
public class SoftballGame {
    @Id
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
    @ColumnDefault("0")
    @Column(name = "forfeited", nullable = false)
    private Boolean forfeited;

    @Size(max = 20)
    @Column(name = "teamAtFault", length = 20)
    private String teamAtFault;

    @Size(max = 20)
    @NotNull
    @Column(name = "winningTeam", nullable = false, length = 20)
    private String winningTeam;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "isMercyWin", nullable = false)
    private Boolean isMercyWin;

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

    public String getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(String roadTeam) {
        this.roadTeam = roadTeam;
    }

    public Boolean getForfeited() {
        return forfeited;
    }

    public void setForfeited(Boolean forfeited) {
        this.forfeited = forfeited;
    }

    public String getTeamAtFault() {
        return teamAtFault;
    }

    public void setTeamAtFault(String teamAtFault) {
        this.teamAtFault = teamAtFault;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Boolean getIsMercyWin() {
        return isMercyWin;
    }

    public void setIsMercyWin(Boolean isMercyWin) {
        this.isMercyWin = isMercyWin;
    }

}