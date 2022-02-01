package it.vodafone.test.service;

import it.vodafone.test.dto.PersonTaxCode;
import it.vodafone.test.entity.Country;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.repository.CountryRepository;
import it.vodafone.test.util.StringUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PersonTaxCodeCreator {

    private final MonthParser monthParser;
    private final CountryRepository countryRepository;
    private final CinCalculator cinCalculator;

    /**
     * It joins all the components calculated starting from the inputs to create the tax code
     * @param personTaxCode
     * @return
     */
    public String createTaxCode(PersonTaxCode personTaxCode) {

        String surnameSection = getSurnameSection(personTaxCode);
        String nameSection = getNameSection(personTaxCode);
        LocalDate birthDate = personTaxCode.getBirthDate();

        String year = valueOf(birthDate.getYear()).substring(2, 4);
        String monthLetter = monthParser.getMonthLetter(birthDate.getMonth());
        String birthDay = extractBirthDay(personTaxCode);

        Optional<Country> optionalCity = countryRepository.findByName(personTaxCode.getCountry().toUpperCase());
        Country country = optionalCity.get();

        String baseTaxCode = surnameSection + nameSection + year + monthLetter + birthDay + country.getCode();

        String controlCharacter = cinCalculator.getControlCharacter(baseTaxCode);

        return baseTaxCode + controlCharacter;
    }

    /**
     * It extracts the birth day from the input date considering the format 00
     * @param personTaxCode
     * @return
     */
    String extractBirthDay(PersonTaxCode personTaxCode) {
        NumberFormat numberFormat = new DecimalFormat("00");
        Gender genderFromTaxCode = personTaxCode.getGenderFromTaxCode();
        int birthDay = personTaxCode.getBirthDate().getDayOfMonth();
        if (genderFromTaxCode == Gender.FEMALE) {
            birthDay = birthDay + 40;
        }

        return numberFormat.format(birthDay);
    }

    /**
     * It extracts the surname section form the taxcode following the following rules:
     * 1- surname length = 2 -> add a 'X' at the end
     * 2- take the first 3 consonants from all the surnames in the order and if not enough
     * even the vowels always following the order
     * @param personTaxCode
     * @return
     */
    String getSurnameSection(PersonTaxCode personTaxCode) {
        List<String> surnames = personTaxCode.getSurname()
                .stream()
                .map(String::toUpperCase)
                .collect(toList());;

        String consonants = surnames.stream()
                .map(StringUtils::stripAccents)
                .flatMapToInt(String::chars)
                .mapToObj(s -> (char) s)
                .map(String::valueOf)
                .filter(s -> !StringUtil.isVowel(s))
                .limit(3)
                .collect(Collectors.joining(""));

        if (consonants.length() < 3) {
            String vowels = surnames.stream()
                    .map(StringUtils::stripAccents)
                    .flatMapToInt(String::chars)
                    .mapToObj(s -> (char) s)
                    .map(String::valueOf)
                    .filter(StringUtil::isVowel)
                    .limit(3 - consonants.length())
                    .collect(Collectors.joining(""));
            consonants = consonants + vowels;
        }

        if (surnames.size() == 1 && surnames.get(0).length() < 3) {
            consonants = consonants + "X";
        }

        return consonants;
    }

    /**
     * It extracts the surname section form the taxcode following the following rules:
     * 1- name length = 2 -> add a 'X' at the end
     * 2- more than 3 consonants -> append the first, the third and the fourth
     * 3- take the first 3 consonants from all the name in the order and if not enough
     * even the vowels always following the order
     * @param personTaxCode
     * @return
     */
    String getNameSection(PersonTaxCode personTaxCode) {
        List<String> name = personTaxCode.getName()
                .stream()
                .map(String::toUpperCase)
                .collect(toList());

        if (!name.isEmpty() && name.get(0).chars().mapToObj(s -> (char) s)
                .map(String::valueOf)
                .filter(s -> !StringUtil.isVowel(s)).count() > 3L) {

            List<String> firstFourConsonants = name.get(0).chars()
                    .mapToObj(s -> (char) s)
                    .map(String::valueOf)
                    .filter(s -> !StringUtil.isVowel(s))
                    .limit(4)
                    .collect(toList());

            return firstFourConsonants.get(0) + firstFourConsonants.get(2) + firstFourConsonants.get(3);
        }

        String consonants = name.stream()
                .map(StringUtils::stripAccents)
                .flatMapToInt(String::chars)
                .mapToObj(s -> (char) s)
                .map(String::valueOf)
                .filter(s -> !StringUtil.isVowel(s))
                .limit(3)
                .collect(Collectors.joining(""));

        if (consonants.length() < 3) {
            String vowels = name.stream()
                    .map(StringUtils::stripAccents)
                    .flatMapToInt(String::chars)
                    .mapToObj(s -> (char) s)
                    .map(String::valueOf)
                    .filter(StringUtil::isVowel)
                    .limit(3 - consonants.length())
                    .collect(Collectors.joining(""));
            consonants = consonants + vowels;
        }

        if (name.size() == 1 && name.get(0).length() < 3) {
            consonants = consonants + "X";
        }

        return consonants;
    }


}
