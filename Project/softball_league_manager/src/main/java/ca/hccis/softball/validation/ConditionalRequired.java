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
@Constraint(validatedBy = ConditionalRequired.Validator.class)
@Documented
@Repeatable(ConditionalRequired.List.class)
public @interface ConditionalRequired {
    String message() default "This field is required when the condition is true.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String conditionalField(); // The name of the field that determines validity.
    String dependentField(); // The name of the field that depends on the value of another to be required.
    boolean expectedValue() default true;

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ConditionalRequired[] value();
    }

    public static class Validator implements ConstraintValidator<ConditionalRequired, GameDTO> {
        private String conditionalField;
        private String dependentField;
        private boolean expectedValue;
        private String message;

        @Override
        public void initialize(ConditionalRequired constraintAnnotation) {
            this.conditionalField = constraintAnnotation.conditionalField();
            this.dependentField = constraintAnnotation.dependentField();
            this.expectedValue = constraintAnnotation.expectedValue();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
            Object condValue = wrapper.getPropertyValue(conditionalField);
            Object depValue = wrapper.getPropertyValue(dependentField);

            boolean isConditionMet = Boolean.valueOf(expectedValue).equals(condValue);

            if (isConditionMet) {
                boolean isValid = depValue != null && !depValue.toString().trim().isEmpty();
                if (!isValid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(dependentField)
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
