package it.vodafone.test.validator;

import it.vodafone.test.service.MonthParser;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class TaxCodeValidatorUtil {

    private final static String ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";
    private final static String LITTERAL_REGEX = "[a-zA-Z]+";
    private final List<Integer> allowedLength = Arrays.asList(9, 11, 16);

    private final static Pattern alphanumeric = Pattern.compile(ALPHANUMERIC_REGEX);
    private final static Pattern litteral = Pattern.compile(LITTERAL_REGEX);

    private final MonthParser monthParser;

    public boolean checkAlphanumericCharacters(String input) {
        Matcher matcher = alphanumeric.matcher(input);
        return matcher.matches();
    }

    public boolean checkLitteralCharacters(String input) {
        Matcher matcher = litteral.matcher(input);
        return matcher.matches();
    }

    public boolean checkMonth(String month) {
        return monthParser.isValidMonth(month.toUpperCase());
    }

    public boolean checkCountry(String country) {
        return checkLitteralCharacters(country.substring(0, 1)) && StringUtils.isNumeric(country.substring(1, 4));
    }

    public Boolean checkStringLength(String taxCode) {
        return allowedLength.contains(taxCode.length());
    }
}
