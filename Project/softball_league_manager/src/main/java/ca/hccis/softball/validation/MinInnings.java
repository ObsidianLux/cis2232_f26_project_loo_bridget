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
@Constraint(validatedBy = MinInnings.Validator.class)
@Documented
public @interface MinInnings {
    String message() default "Minimum number of innings not met.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String innings();
    long minSize() default 5;

    public static class Validator implements ConstraintValidator<MinInnings, GameDTO> {
        private String innings;
        private long minSize;
        private String message;

        @Override
        public void initialize(MinInnings constraintAnnotation) {
            this.innings = constraintAnnotation.innings();
            this.minSize = constraintAnnotation.minSize();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
                Object listInnings = wrapper.getPropertyValue(innings);

                // If empty or null, skip this validation (let @NotEmpty or @NotNull handle it)
                if (!(listInnings instanceof Collection<?>) || ((Collection<?>) listInnings).isEmpty()) {
                    return true;
                }

                Collection<?> collection = (Collection<?>) listInnings;

                if (collection.size() < minSize) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(innings)
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
