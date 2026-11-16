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
@Constraint(validatedBy = MinInnings.Validator.class)
@Documented
public @interface MinInnings {
    String message() default "Minimum number of valid innings not met.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // The name of the innings attribute.
    String innings();

    // By default, there must be at least 7 innings to be valid.
    long minSize() default 7;

    // Whether the list of innings is required or not.
    String requiredField();

    class Validator implements ConstraintValidator<MinInnings, GameDTO> {
        private String listField;
        private long minSize;
        private String requiredField;
        private String message;

        @Override
        public void initialize(MinInnings constraintAnnotation) {
            this.listField = constraintAnnotation.innings();
            this.minSize = constraintAnnotation.minSize();
            this.requiredField = constraintAnnotation.requiredField();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

                // Get the list of innings.
                Object listInnings = wrapper.getPropertyValue(listField);

                // Get the size of the list of innings. If it's not a collection object, return 0.
                int size = listInnings instanceof Collection<?> c ? c.size() : 0;

                // Check whether the innings list is required to pass validation. For example, a forfeited game does not require innings.
                boolean required = Boolean.TRUE.equals(wrapper.getPropertyValue(requiredField));

                // Games that haven't been played or were forfeited have no innings, so there's nothing to check.
                if (size == 0 && !required) return true;

                // If the number of innings is fewer than the minimum allowed, the validation has failed.
                if (size < minSize) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(listField)
                            .addConstraintViolation();
                    return false;
                }

                // If there is a valid number of innings, make sure at least one of them has a value for either home runs or road runs. 
                boolean hasAtLeastOneRunValue = false;
                
                if (listInnings instanceof Collection<?> innings) {
                    for (Object item : innings) {
                        if (item instanceof Inning inning) {
                            if (inning.getHomeRuns() != 0 || inning.getRoadRuns() != 0) {
                                hasAtLeastOneRunValue = true;
                                break;
                            }
                        }
                    }
                }
                
                // If no innings have any values, the validation has failed.
                if (!hasAtLeastOneRunValue) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(listField)
                            .addConstraintViolation();
                    return false;
                }
            } catch (Exception e) {
                // Safely fail validation if properties do not map correctly.
                return false;
            }

            return true;
        }
    }
}
