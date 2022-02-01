package it.vodafone.test.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingCountry {
    String message() default "There is no country or city in the database for the selected name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
