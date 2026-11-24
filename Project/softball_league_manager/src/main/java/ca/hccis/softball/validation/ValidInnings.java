package ca.hccis.softball.validation;

import ca.hccis.softball.model.bo.GameBO;
import ca.hccis.softball.model.dto.GameRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDate;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidInnings.Validator.class)
@Documented
public @interface ValidInnings {
    String message() default "Invalid innings.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidInnings, GameRequest> {
        @Override
        public boolean isValid(GameRequest dto, ConstraintValidatorContext ctx) {
            // There's nothing to validate in the first place.
            if (dto == null) return true;

            // If gameDay is null, this validation won't work, so end early and let gameDay's constraints handle it.
            LocalDate gameDay = dto.gameDay();
            if (gameDay == null) return true;

            // Check if the game was played. If not, this validation isn't required.
            if (!GameBO.isPlayed(gameDay)) return true;

            // Check if the game was forfeited. If yes, add an error message if the team at fault is missing or if the team at fault doesn't match either home team or away team.
            if (Boolean.TRUE.equals(dto.forfeited())) {
                String teamAtFault = dto.teamAtFault();

                if (teamAtFault == null || teamAtFault.isBlank()) {
                    ctx.disableDefaultConstraintViolation();
                    ctx.buildConstraintViolationWithTemplate("The team who forfeited the game is required.")
                            .addPropertyNode("teamAtFault")
                            .addConstraintViolation();
                    return false;
                }

                if (!teamAtFault.equalsIgnoreCase(dto.homeTeamName()) && !teamAtFault.equalsIgnoreCase(dto.awayTeamName())) {
                    ctx.disableDefaultConstraintViolation();
                    ctx.buildConstraintViolationWithTemplate("The team who forfeited the game must match either the home team or the away team.")
                            .addPropertyNode("teamAtFault")
                            .addConstraintViolation();
                    return false;
                }
            } else {
                // If the game was not forfeited, check to see if either matches the number of innings required for the mercy rule to apply or the minimum number of innings in a standard game.
                int numberOfInnings = dto.innings().size();

                if (!(numberOfInnings == GameBO.MERCY_RULE_INNINGS || numberOfInnings >= GameBO.MINIMUM_INNINGS_STANDARD)) {
                    ctx.disableDefaultConstraintViolation();
                    ctx.buildConstraintViolationWithTemplate("A game must have at least %d innings unless it ended through the application of the Mercy Rule, in which case the number of innings must be %d.".formatted(GameBO.MINIMUM_INNINGS_STANDARD, GameBO.MERCY_RULE_INNINGS))
                            .addPropertyNode("innings")
                            .addConstraintViolation();
                    return false;
                }

                // If the number of innings is valid, make sure the total scores aren't both 0.
                int homeRuns = GameBO.getHomeRuns(dto);
                int awayRuns = GameBO.getAwayRuns(dto);

                if (homeRuns == 0 && awayRuns == 0) {
                    ctx.disableDefaultConstraintViolation();
                    ctx.buildConstraintViolationWithTemplate("A game must end with at least one team's final score being greater than 0.")
                            .addPropertyNode("innings")
                            .addConstraintViolation();
                    return false;
                }

                // If the total scores aren't 0, check to see if the number of innings matches the requirement for the mercy rule and check if the difference in scores is great enough.
                if (numberOfInnings == GameBO.MERCY_RULE_INNINGS && Math.abs(homeRuns - awayRuns) < GameBO.MERCY_DIFFERENCE_MINIMUM) {
                    ctx.disableDefaultConstraintViolation();
                    ctx.buildConstraintViolationWithTemplate("For games that ended through the application of the Mercy Rule, there must be a difference of at least %d between the teams' final scores.".formatted(GameBO.MERCY_DIFFERENCE_MINIMUM))
                            .addPropertyNode("innings")
                            .addConstraintViolation();
                    return false;
                }

                // If the game didn't end because of the Mercy Rule, check to see if the two teams' scores are equal.
                if (homeRuns == awayRuns) {
                    ctx.disableDefaultConstraintViolation();
                    ctx.buildConstraintViolationWithTemplate("A game cannot end in a tie.")
                            .addPropertyNode("innings")
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
