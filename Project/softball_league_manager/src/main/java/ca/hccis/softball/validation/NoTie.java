package ca.hccis.softball.validation;

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
@Constraint(validatedBy = NoTie.Validator.class)
@Documented
public @interface NoTie {
    String message() default "The game cannot end in a tie.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String listField();
    String firstScore();
    String secondScore();

    public static class Validator implements ConstraintValidator<NoTie, GameDTO> {
        private String listField;
        private String firstScore;
        private String secondScore;
        private String message;

        @Override
        public void initialize(NoTie constraintAnnotation) {
            this.listField = constraintAnnotation.listField();
            this.firstScore = constraintAnnotation.firstScore();
            this.secondScore = constraintAnnotation.secondScore();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

                // No innings means there is nothing to compare (not played or forfeited).
                if (!(wrapper.getPropertyValue(listField) instanceof Collection<?> innings) || innings.isEmpty()) {
                    return true;
                }

                Object firstValue = wrapper.getPropertyValue(firstScore);
                Object secondValue = wrapper.getPropertyValue(secondScore);

                if (firstValue != null && firstValue.equals(secondValue)) {
                    context.disableDefaultConstraintViolation();
                    // Attach the error to the list property node so Thymeleaf can reference it.
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
