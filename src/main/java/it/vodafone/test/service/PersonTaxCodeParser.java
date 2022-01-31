package it.vodafone.test.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

@Service
@AllArgsConstructor
public class PersonTaxCodeParser {

    private final OmocodeTransformer omocodeTransformer;
    private final MonthParser monthParser;

    public String getSurname(String taxCode) {
        return taxCode.substring(0, 3); // characters
    }

    public String getName(String taxCode) {
        return taxCode.substring(3, 6); // characters
    }

    public String getYear(String taxcode) {
        return taxcode.substring(6, 8); // two digits
    }

    public String getCompleteBirthYear(String taxCode) {
        String rawYear = getYear(taxCode); // two digits
        String cleanedTaxCodeYear = omocodeTransformer.cleanFromOmocodeCharacters(rawYear);
        String actualYear = String.valueOf(LocalDate.now().getYear()).substring(2, 4);

        if (Integer.parseInt(cleanedTaxCodeYear) > Integer.parseInt(actualYear)) {
            return "19" + cleanedTaxCodeYear;
        }

        return "20" + cleanedTaxCodeYear;
    }

    public String getRawMonth(String taxCode) {
        return taxCode.substring(8, 9);
    }

    public Month getBirthMonth(String taxCode) {
        String birthMonth = taxCode.substring(8, 9); // to be transformed in months
        return monthParser.getMonth(birthMonth);
    }

    public String getBirthDay(String taxCode) {
        String birthDay = taxCode.substring(9, 11); // digits
        return omocodeTransformer.cleanFromOmocodeCharacters(birthDay);
    }

    public String getCountry(String taxCode) {
        String country = taxCode.substring(11, 15); // a letter and 3  digits
        return country.charAt(0) + omocodeTransformer.cleanFromOmocodeCharacters(country.substring(1, 4));
    }

    public String getControlCharacter(String taxCode) {
        return taxCode.substring(15, 16);
    }

}
