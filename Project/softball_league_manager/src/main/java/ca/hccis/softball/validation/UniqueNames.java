package ca.hccis.softball.validation;

import ca.hccis.softball.model.dto.GameDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNames.Validator.class)
@Documented
public @interface UniqueNames {
    String message() default "The home team's name cannot be the same as the road team's name.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String firstName();
    String secondName();

    public static class Validator implements ConstraintValidator<UniqueNames, GameDTO> {
        private String firstName;
        private String secondName;
        private String message;

        @Override
        public void initialize(UniqueNames constraintAnnotation) {
            this.firstName = constraintAnnotation.firstName();
            this.secondName = constraintAnnotation.secondName();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
                Object firstValue = wrapper.getPropertyValue(firstName);
                Object secondValue = wrapper.getPropertyValue(secondName);

                if (firstValue == null || secondValue == null) return true;
                if (isStringAndBlank(firstValue) || isStringAndBlank(secondValue)) return true;

                if (firstValue.equals(secondValue)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(secondName).addConstraintViolation();
                    return false;
                }
            } catch (Exception e) {
                // Safely fail validation if properties do not map correctly.
                return false;
            }

            return true;
        }

        private boolean isStringAndBlank(Object value) {
            if (value instanceof String) {
                // .trim().isEmpty() handles both completely empty "" and spaces "   "
                return ((String) value).trim().isEmpty();
            }
            return false;
        }
    }
}
