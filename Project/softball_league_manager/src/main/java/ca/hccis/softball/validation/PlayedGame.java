package ca.hccis.softball.validation;

import ca.hccis.softball.model.domain.Inning;
import ca.hccis.softball.model.dto.GameDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlayedGame.Validator.class)
@Documented
public @interface PlayedGame {
    String message() default "Minimum number of valid innings not met.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // The name of the innings attribute.
    String innings();

    // Whether the list of innings is required or not.
    String requiredField();

    String homeRuns();
    String roadRuns();

    int mercyInningCount() default 5;
    int mercyMinimumDifference() default 10;

    int standardInningMinimum() default 7;
    String invalidInningsMessage() default "At least one inning must have a score for either the home team or the road team.";
    String noTieMessage() default "The game cannot end in a tie.";
    String mercyRuleMessage() default "The difference in scores is not great enough to trigger the Mercy Rule.";

    class Validator implements ConstraintValidator<PlayedGame, GameDTO> {
        private String requiredField;
        private String innings;

        private String homeRuns;
        private String roadRuns;
        private Integer standardInningMinimum;
        private Integer mercyInningCount;
        private Integer mercyMinimumDifference;

        private String message;
        private String mercyRuleMessage;
        private String invalidInningsMessage;
        private String noTieMessage;

        @Override
        public void initialize(PlayedGame constraintAnnotation) {
            this.requiredField = constraintAnnotation.requiredField();
            this.innings = constraintAnnotation.innings();
            this.standardInningMinimum = constraintAnnotation.standardInningMinimum();
            this.invalidInningsMessage = constraintAnnotation.invalidInningsMessage();
            this.homeRuns = constraintAnnotation.homeRuns();
            this.roadRuns = constraintAnnotation.roadRuns();
            this.message = constraintAnnotation.message();
            this.mercyRuleMessage = constraintAnnotation.mercyRuleMessage();
            this.mercyMinimumDifference = constraintAnnotation.mercyMinimumDifference();
            this.mercyInningCount = constraintAnnotation.mercyInningCount();
            this.noTieMessage = constraintAnnotation.noTieMessage();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            // If there's nothing to check, return true.
            if (value == null) return true;

            try {
                // Get the bean.
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

                // Check if innings are required for the game.
                if (Boolean.TRUE.equals(wrapper.getPropertyValue(requiredField))) {
                    // Get the list of innings.
                    Object listInnings = wrapper.getPropertyValue(innings);

                    // If the innings object isn't a collection, return true.
                    if (!(listInnings instanceof Collection<?> items)) {
                        return true;
                    }

                    // Get the home and road scores.
                    Object objHomeRuns = wrapper.getPropertyValue(homeRuns);
                    Object objRoadRuns = wrapper.getPropertyValue(roadRuns);

                    // TODO Make sure this doesn't cause any issues.
                    if (objHomeRuns == null || objRoadRuns == null) return true;

                    // Get the scores as numbers.
                    Double homeRuns = ((Number) objHomeRuns).doubleValue();
                    Double roadRuns = ((Number) objRoadRuns).doubleValue();

                    int numberOfInnings = items.size();

                    // Check the number of innings to see if the game ended with a valid number of innings for a standard game or one that ended by mercy rule.
                    if (numberOfInnings < standardInningMinimum && numberOfInnings != mercyInningCount) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message)
                                .addPropertyNode(innings)
                                .addConstraintViolation();
                        return false;
                    } else {
                        // If there is a valid number of innings, make sure at least one of them has a value for either home runs or road runs.
                        boolean hasAtLeastOneRunValue = false;

                        for (Object item : items) {
                            if (item instanceof Inning inning) {
                                if (inning.getHomeRuns() != 0 || inning.getRoadRuns() != 0) {
                                    hasAtLeastOneRunValue = true;
                                    break;
                                }
                            }
                        }

                        // If no innings have any values, the validation has failed.
                        if (!hasAtLeastOneRunValue) {
                            context.disableDefaultConstraintViolation();
                            context.buildConstraintViolationWithTemplate(invalidInningsMessage)
                                    .addPropertyNode(innings)
                                    .addConstraintViolation();
                            return false;
                        }

                        // Check if the score difference is less than the required threshold for mercy rule application.
                        if (numberOfInnings == mercyInningCount && Math.abs(homeRuns - roadRuns) < mercyMinimumDifference) {
                            context.disableDefaultConstraintViolation();
                            // Attach the error to the list property node so Thymeleaf can reference it
                            context.buildConstraintViolationWithTemplate(mercyRuleMessage)
                                    .addPropertyNode(innings)
                                    .addConstraintViolation();
                            return false;
                        }

                        // If there is at least one inning with a score, make sure the game didn't end in a tie.
                        if (homeRuns.equals(roadRuns)) {
                            context.disableDefaultConstraintViolation();
                            // Attach the error to the list property node so Thymeleaf can reference it.
                            context.buildConstraintViolationWithTemplate(noTieMessage)
                                    .addPropertyNode(innings)
                                    .addConstraintViolation();
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                // Safely fail validation if properties do not map correctly.
                return false;
            }

            return true;
        }
    }
}
