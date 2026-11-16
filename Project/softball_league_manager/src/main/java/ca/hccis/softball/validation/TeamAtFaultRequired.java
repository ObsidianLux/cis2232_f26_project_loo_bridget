package ca.hccis.softball.validation;

import ca.hccis.softball.model.dto.GameDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.*;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TeamAtFaultRequired.Validator.class)
@Documented
public @interface TeamAtFaultRequired {
    String message() default "The team who forfeited the game is required.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // The name of the field that determines validity.
    String wasForfeited();

    // The name of the field that depends on the value of another to be required.
    String teamAtFault();

    // To apply this validation, wasForfeited should be equal to true.
    boolean expectedValue() default true;

    // Validator for this custom constraint.
    class Validator implements ConstraintValidator<TeamAtFaultRequired, GameDTO> {
        private String wasForfeited;
        private String teamAtFault;
        private boolean expectedValue;
        private String message;

        @Override
        public void initialize(TeamAtFaultRequired constraintAnnotation) {
            this.wasForfeited = constraintAnnotation.wasForfeited();
            this.teamAtFault = constraintAnnotation.teamAtFault();
            this.expectedValue = constraintAnnotation.expectedValue();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            // If there's nothing to check, return true.
            if (value == null) return true;

            // Get the object and it's values to check.
            BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
            Object condValue = wrapper.getPropertyValue(wasForfeited);
            Object depValue = wrapper.getPropertyValue(teamAtFault);

            // If the expected value is the same as the actual conditional value, return true.
            boolean isConditionMet = Boolean.valueOf(expectedValue).equals(condValue);

            // If the game was forfeited, apply the constraint. Otherwise, return true.
            if (isConditionMet) {
                // Check if the dependent value exists and isn't empty.
                boolean isValid = depValue != null && !depValue.toString().trim().isEmpty();

                // If the dependent value isn't valid, flag it and add the constraints message to the list of errors.
                if (!isValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(teamAtFault)
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
