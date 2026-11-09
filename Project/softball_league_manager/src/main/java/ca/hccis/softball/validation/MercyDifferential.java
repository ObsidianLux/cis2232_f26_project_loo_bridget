package ca.hccis.softball.validation;

import ca.hccis.softball.entity.GameDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MercyDifferential.Validator.class)
@Documented
public @interface MercyDifferential {
    String message() default "The difference in scores is not great enough to trigger the Mercy Rule.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String listField();
    String homeRuns();
    String roadRuns();
    long numberOfInnings() default 5;
    long minDifference() default 10;

    public static class Validator implements ConstraintValidator<MercyDifferential, GameDTO> {
        private String listField;
        private String homeRuns;
        private String roadRuns;
        private long minDifference;
        private long numberOfInnings;
        private String message;

        @Override
        public void initialize(MercyDifferential constraintAnnotation) {
            this.listField = constraintAnnotation.listField();
            this.homeRuns = constraintAnnotation.homeRuns();
            this.roadRuns = constraintAnnotation.roadRuns();
            this.minDifference = constraintAnnotation.minDifference();
            this.numberOfInnings = constraintAnnotation.numberOfInnings();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(GameDTO value, ConstraintValidatorContext context) {
            if (value == null) return true;

            try {
                BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
                Object collectionObj = wrapper.getPropertyValue(listField);

                // If empty or null, skip this validation (let @NotEmpty or @NotNull handle it)
                if (!(collectionObj instanceof Collection<?>) || ((Collection<?>) collectionObj).isEmpty()) {
                    return true;
                }

                Collection<?> items = (Collection<?>) collectionObj;

                // If the number of innings isn't 5, skip this validation.
                if (items.size() != numberOfInnings) return true;

                double sum1 = 0;
                double sum2 = 0;

                // Iterate through the ArrayList elements and aggregate target property values
                for (Object item : items) {
                    if (item != null) {
                        BeanWrapperImpl itemWrapper = new BeanWrapperImpl(item);
                        Object val1 = itemWrapper.getPropertyValue(homeRuns);
                        Object val2 = itemWrapper.getPropertyValue(roadRuns);

                        if (val1 instanceof Number) sum1 += ((Number) val1).doubleValue();
                        if (val2 instanceof Number) sum2 += ((Number) val2).doubleValue();
                    }
                }

                // Check if the difference is greater than the required threshold
                if (Math.abs(sum1 - sum2) <= minDifference) {
                    context.disableDefaultConstraintViolation();
                    // Attach the error to the list property node so Thymeleaf can reference it
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(listField)
                            .addConstraintViolation();
                    return false;
                }

            } catch (Exception e) {
                return false; // Safely fail validation if properties do not map correctly
            }

            return true;
        }
    }
}
