package it.vodafone.test.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

/**
 * It checks if a string list contains null value or is composed by a single character
 */
@Service
@AllArgsConstructor
public class InvalidListValidator implements
        ConstraintValidator<InvalidList, List<String>> {


    @Override
    public boolean isValid(List<String> list, ConstraintValidatorContext constraintValidatorContext) {

        if (list != null && list.isEmpty()) {
            return true;
        }
        return !(list.stream().anyMatch(Objects::isNull) || list.get(0).length() == 1);
    }

}
