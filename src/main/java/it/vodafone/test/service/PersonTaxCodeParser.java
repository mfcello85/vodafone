package it.vodafone.test.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;

@Service
@AllArgsConstructor
public class PersonTaxCodeParser {

    private final OmocodeTransformer omocodeTransformer;
    private final MonthParser monthParser;

    /**
     * It extract the surname field as the first 3 characters
     * @param taxCode
     * @return
     */
    public String getSurname(String taxCode) {
        return taxCode.substring(0, 3); // characters
    }

    /**
     * It extracts the name field as the string between the fourth and the sixth characters
     * @param taxCode
     * @return
     */
    public String getName(String taxCode) {
        return taxCode.substring(3, 6); // characters
    }

    /**
     * Extract the year field as the string between the seventh and the eighth characters cleaning the result
     * from eventual letter interted to avoid tax code collision
     * @param taxcode
     * @return
     */
    public String getYear(String taxcode) {
        return omocodeTransformer.cleanFromOmocodeCharacters(taxcode.substring(6, 8)); // two digits
    }

    /**
     * It extracts the birth year and append the remaining digits following this logic_
     * if the parsed year is greater the current than 19XX else 20XX
     * @param taxCode
     * @return
     */
    public String getCompleteBirthYear(String taxCode) {
        String cleanedTaxCodeYear = getYear(taxCode);
        String actualYear = String.valueOf(LocalDate.now().getYear()).substring(2, 4);

        if (Integer.parseInt(cleanedTaxCodeYear) > Integer.parseInt(actualYear)) {
            return "19" + cleanedTaxCodeYear;
        }

        return "20" + cleanedTaxCodeYear;
    }

    /**
     * It extracts the birth month field as the string between the nineth and the tenth characters
     * @param taxCode
     * @return
     */
    public String getRawMonth(String taxCode) {
        return taxCode.substring(8, 9);
    }

    /**
     * It extracts the Month as the nineth and the tenth characters converted according
     * to the conversion table
     * @param taxCode
     * @return
     */
    public Month getBirthMonth(String taxCode) {
        String birthMonth = taxCode.substring(8, 9); // to be transformed in months
        return monthParser.getMonth(birthMonth);
    }

    /**
     * It extracts the Month as the string between the nineth and tenth characters cleaned
     * from eventual letters interted to avoid tax code collision
     * @param taxCode
     * @return
     */
    public String getBirthDay(String taxCode) {
        String birthDay = taxCode.substring(9, 11); // digits
        String cleanedBithDay = omocodeTransformer.cleanFromOmocodeCharacters(birthDay);
        int rawDay = Integer.parseInt(cleanedBithDay);
        if(rawDay>40){
            rawDay = rawDay - 40;
        }
        NumberFormat numberFormat = new DecimalFormat("00");
        return numberFormat.format(rawDay);
    }

    /**
     * It extracts the country code as the string between the twelveth and the fourteenth characters with the last
     * from eventual letters interted to avoid tax code collision
     * @param taxCode
     * @return
     */
    public String getCountry(String taxCode) {
        String country = taxCode.substring(11, 15); // a letter and 3  digits
        return country.charAt(0) + omocodeTransformer.cleanFromOmocodeCharacters(country.substring(1, 4));
    }

}
