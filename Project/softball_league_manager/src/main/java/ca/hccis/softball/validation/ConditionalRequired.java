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

    // The name of the field that determines validity.
    String conditionalField();

    // The name of the field that depends on the value of another to be required.
    String dependentField();

    boolean expectedValue() default true;

    // This interface allows this validator to be applied more than once to a single class.
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ConditionalRequired[] value();
    }

    // Validator for this custom constraint.
    class Validator implements ConstraintValidator<ConditionalRequired, GameDTO> {
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
            // If there's nothing to check, return true.
            if (value == null) return true;

            // Get the object and it's values to check.
            BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
            Object condValue = wrapper.getPropertyValue(conditionalField);
            Object depValue = wrapper.getPropertyValue(dependentField);

            // If the expected value is the same as the actual conditional value, return true.
            boolean isConditionMet = Boolean.valueOf(expectedValue).equals(condValue);

            // If the condition is met, apply the constraint. Otherwise, return true.
            if (isConditionMet) {
                // check if the dependent value exists and isn't empty.
                boolean isValid = depValue != null && !depValue.toString().trim().isEmpty();

                // If the dependent value isn't valid, flag it and add the constraints message to the list of errors.
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
