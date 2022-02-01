package it.vodafone.test.validator;

import it.vodafone.test.repository.CityRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@AllArgsConstructor
public class CountryValidator implements
        ConstraintValidator<ExistingCountry, String> {
    private final CityRepository cityRepository;

    @Override
    public void initialize(ExistingCountry constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String city, ConstraintValidatorContext constraintValidatorContext) {

        if (StringUtils.isBlank(city)) {
            return true;
        }
        return cityRepository.findByName(city.toUpperCase()).isPresent();
    }

}
