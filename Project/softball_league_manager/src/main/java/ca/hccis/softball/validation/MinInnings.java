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
    String requiredField();

    public static class Validator implements ConstraintValidator<MinInnings, GameDTO> {
        private String innings;
        private long minSize;
        private String requiredField;
        private String message;

        @Override
        public void initialize(MinInnings constraintAnnotation) {
            this.innings = constraintAnnotation.innings();
            this.minSize = constraintAnnotation.minSize();
            this.requiredField = constraintAnnotation.requiredField();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);

                Object listInnings = wrapper.getPropertyValue(innings);
                int size = listInnings instanceof Collection<?> c ? c.size() : 0;
                boolean required = Boolean.TRUE.equals(wrapper.getPropertyValue(requiredField));

                // Games that haven't been played or were forfeited have no innings, so there's nothing to check.
                if (size == 0 && !required) return true;

                if (size < minSize) {
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
