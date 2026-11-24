package ca.hccis.softball.model.dto;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.entity.Game;
import ca.hccis.softball.validation.UniqueTeamNames;
import ca.hccis.softball.validation.ValidInnings;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UniqueTeamNames
@ValidInnings
public record GameRequest(
        Integer id,

        @Size(max = 30, message = "A team's name must not exceed 30 characters.")
        @NotBlank(message = "The home team's name is required.")
        String homeTeamName,

        @Size(max = 30, message = "A team's name must not exceed 30 characters.")
        @NotBlank(message = "The away team's name is required.")
        String awayTeamName,

        @NotNull(message = "The scheduled game date is required.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate gameDay,

        Boolean forfeited,
        String teamAtFault,

        @Valid
        List<InningRequest> innings
) {
    /**
     * Compact constructor that always runs when an instance is created regardless of how it was created.
     * This is where data normalization should occur so that it's consistent.
     */
    public GameRequest {
        // Protect against null lists of innings.
        if (innings == null) innings = List.of();

        // Protect against NullPointerException when trimming team names.
        homeTeamName = homeTeamName == null ? null : homeTeamName.trim();
        awayTeamName = awayTeamName == null ? null : awayTeamName.trim();
        teamAtFault = teamAtFault == null ? null : teamAtFault.trim();
    }

    /**
     * No-arg constructor for clean initialization.
     */
    public GameRequest() {
        this(null, null, null, null, null, null, null);
    }

    public static GameRequest from(Game game) {
        return new GameRequest(
                    game.getId(),
                    game.getHomeTeam(),
                    game.getAwayTeam(),
                    game.getDate(),
                    game.getWasForfeited(),
                    game.getTeamAtFault(),
                    game.getInnings() == null ? new ArrayList<>() : game.getInnings().stream().map(InningRequest::from).toList()
            );
    }

    public Game toEntity() {
            Game game = new Game();

            // Set the attributes that are shared across all games.
            game.setId(id);
            game.setHomeTeam(homeTeamName);
            game.setAwayTeam(awayTeamName);
            game.setDate(gameDay);

            // If the game is upcoming, the rest of the attributes are left null.
            if (GameBO.isPlayed(gameDay)) {
                game.setWasForfeited(Boolean.TRUE.equals(forfeited));

                // If the game was forfeited, it's handled differently.
                if (game.getWasForfeited()) {
                    game.setTeamAtFault(teamAtFault);
                } else {
                    game.setInnings(innings.stream().map(InningRequest::toEntity).toList());
                }
            }

            return game;
    }

    public String winningTeamName() {
        return GameBO.getWinningTeam(this);
    }

    public boolean mercyWin() {
        return innings != null && innings.size() == GameBO.MERCY_RULE_INNINGS;
    }

    public int homeRuns() {
        return GameBO.getHomeRuns(this);
    }

    public int awayRuns() {
        return GameBO.getAwayRuns(this);
    }

    public boolean played() {
        return GameBO.isPlayed(gameDay);
    }

    public String plusMinus(String teamName) {
        return GameBO.getPlusMinus(this, teamName);
    }
}
