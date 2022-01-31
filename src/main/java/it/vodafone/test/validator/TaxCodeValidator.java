package it.vodafone.test.validator;

import it.vodafone.test.service.MonthParser;
import it.vodafone.test.service.PersonTaxCodeParser;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaxCodeValidator implements
        ConstraintValidator<ValidTaxCode, String> {
    static final String WRONG_LENGTH = "Wrong length: the tax code length must be 9,1, or 16";
    public static final String ALPHANUMERIC_CHARACTER = "A tax code can contain only alphanumeric character";
    public static final String SURNAME_ERROR = "The tax code surname field con contain only literal characters";
    public static final String NAME_ERROR = "The tax code name field con contain" +
            " only literal characters";
    public static final String YEAR_ERROR = "The tax code year field con contain " +
            "only numeric characters";
    public static final String TAX_CODE_DAY_FIELD_NOT_NUMERIC = "The tax code day field con contain " +
            "only numeric characters";
    public static final String WRONG_BIRTH_DAY = "The tax code day field con contain " +
            "only a number between 1 and 31 for male and between 41 and 71 for female";
    public static final String COUNTRY_CODE_ERROR = "The tax code country field is composed of a letter followed " +
            "by three digits eventually replaced by special characted for omocode situations";
    private final List<Integer> allowedLength = Arrays.asList(9, 11, 16);

    private final PersonTaxCodeParser personTaxCodeParser;
    private final TaxCodeValidatorUtil taxCodeValidatorUtil;
    private final MonthParser monthParser;

    @Override
    public boolean isValid(String taxCode, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();

        boolean valid = true;

        if (!taxCodeValidatorUtil.checkStringLength(taxCode)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(WRONG_LENGTH).addConstraintViolation();
            return false;
        }

        // optimization
        if (!taxCodeValidatorUtil.checkAlphanumericCharacters(taxCode)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(ALPHANUMERIC_CHARACTER).addConstraintViolation();
            return false;
        }

        if (taxCode.length() == 16) {
            String surname = personTaxCodeParser.getSurname(taxCode);
            if (!taxCodeValidatorUtil.checkLitteralCharacters(surname)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(SURNAME_ERROR).addConstraintViolation();
                valid = false;
            }

            String name = personTaxCodeParser.getName(taxCode);
            if (!taxCodeValidatorUtil.checkLitteralCharacters(name)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(NAME_ERROR).addConstraintViolation();
                valid = false;
            }

            String year = personTaxCodeParser.getYear(taxCode);
            if (!StringUtils.isNumeric(year)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(YEAR_ERROR).addConstraintViolation();
                valid = false;
            }

            String month = personTaxCodeParser.getRawMonth(taxCode);
            if (!taxCodeValidatorUtil.checkMonth(month)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate("The tax code month field con contain " +
                        "only the literal characters : " + monthParser.getAvailableCharacters()
                        .stream()
                        .collect(Collectors.joining(","))).addConstraintViolation();
                valid = false;
            }

            String birthDay = personTaxCodeParser.getBirthDay(taxCode);
            if (!StringUtils.isNumeric(birthDay)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(TAX_CODE_DAY_FIELD_NOT_NUMERIC).addConstraintViolation();
                valid = false;
            }

            if (StringUtils.isNumeric(birthDay) && notValidBirthDay(birthDay)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(WRONG_BIRTH_DAY).addConstraintViolation();
                valid = false;
            }

            String country = personTaxCodeParser.getCountry(taxCode);
            if (!taxCodeValidatorUtil.checkCountry(country)) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(COUNTRY_CODE_ERROR).addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }

    private boolean notValidBirthDay(String birthDay) {
        return !((Integer.parseInt(birthDay) >= 1 && Integer.parseInt(birthDay) <= 31) || Integer.parseInt(birthDay) >= 41 && Integer.parseInt(birthDay) <= 71);
    }
}
