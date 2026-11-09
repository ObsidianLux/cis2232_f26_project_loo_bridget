package ca.hccis.softball.validation;

import ca.hccis.softball.entity.GameDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoTie.Validator.class)
@Documented
public @interface NoTie {
    String message() default "The game cannot end in a tie.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String firstScore();
    String secondScore();

    public static class Validator implements ConstraintValidator<NoTie, GameDTO> {
        private String firstScore;
        private String secondScore;
        private String message;

        @Override
        public void initialize(NoTie constraintAnnotation) {
            this.firstScore = constraintAnnotation.firstScore();
            this.secondScore = constraintAnnotation.secondScore();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

                Object firstValue = wrapper.getPropertyValue(firstScore);
                Object secondValue = wrapper.getPropertyValue(secondScore);

                if (firstValue == null || secondValue == null) return true;
                if (isEqualToZero(firstValue) && isEqualToZero(secondValue)) return true;

                if (firstValue.equals(secondValue)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode("innings")
                            .addConstraintViolation();
                    return false;
                }
            } catch (Exception e) {
                // Safely fail validation if properties do not map correctly.
                return false;
            }

            return true;
        }

        private boolean isEqualToZero(Object value) {
            if (value == null) return false;

            // Safely check if the number equals 0 across any numeric type
            if (value instanceof Number) {
                return ((Number) value).doubleValue() == 0;
            }
            return false;
        }
    }
}
