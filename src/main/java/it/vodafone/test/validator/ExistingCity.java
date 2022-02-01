package it.vodafone.test.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CityValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingCity {
    String message() default "There is no country or city in the database for the selected name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
