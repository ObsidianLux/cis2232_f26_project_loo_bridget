package ca.hccis.softball.validation;

import ca.hccis.softball.model.dto.GameRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTeamNames.Validator.class)
@Documented
public @interface UniqueTeamNames {
    String message() default "The two team names must not be identical to each other.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<UniqueTeamNames, GameRequest> {
        @Override
        public boolean isValid(GameRequest dto, ConstraintValidatorContext ctx) {
            // There's nothing to check in the first place.
            if (dto == null) return true;

            String homeTeam = dto.homeTeamName();
            String awayTeam = dto.awayTeamName();

            // They can't be identical if either of them has no value.
            if (homeTeam == null || homeTeam.isBlank() || awayTeam == null || awayTeam.isBlank()) return true;

            // Uniqueness check is case-insensitive.
            if (homeTeam.equalsIgnoreCase(awayTeam)) {
                ctx.disableDefaultConstraintViolation();
                ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                        .addPropertyNode("awayTeamName")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
