package it.vodafone.test.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = TaxCodeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaxCode {
    String message() default "Invalid taxcode";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
