package it.vodafone.test.service;

import it.vodafone.test.dto.PersonTaxCode;
import it.vodafone.test.entity.City;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.repository.CityRepository;
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

@Service
@AllArgsConstructor
public class PersonTaxCodeCreator {

    private final MonthParser monthParser;
    private final CityRepository cityRepository;
    private final CinCalculator cinCalculator;

    public String createTaxCode(PersonTaxCode personTaxCode) {

        String surnameSection = getSurnameSection(personTaxCode);
        String nameSection = getNameSection(personTaxCode);
        LocalDate birthDate = personTaxCode.getBirthDate();

        String year = valueOf(birthDate.getYear()).substring(2, 4);
        String monthLetter = monthParser.getMonthLetter(birthDate.getMonth());
        String birthDay = extractBirthDay(personTaxCode);

        Optional<City> optionalCity = cityRepository.findByName(personTaxCode.getCountry().toUpperCase());
        City city = optionalCity.get();

        String baseTaxCode = surnameSection + nameSection + year + monthLetter + birthDay + city.getCode();

        String controlCharacter = cinCalculator.getControlCharacter(baseTaxCode);

        return baseTaxCode + controlCharacter;
    }

    String extractBirthDay(PersonTaxCode personTaxCode) {
        NumberFormat numberFormat = new DecimalFormat("00");
        Gender genderFromTaxCode = personTaxCode.getGenderFromTaxCode();
        int birthDay = personTaxCode.getBirthDate().getDayOfMonth();
        if (genderFromTaxCode == Gender.FEMALE) {
            birthDay = birthDay + 40;
        }

        return numberFormat.format(birthDay);
    }

    String getSurnameSection(PersonTaxCode personTaxCode) {
        List<String> surnames = personTaxCode.getSurname();

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

    String getNameSection(PersonTaxCode personTaxCode) {
        List<String> name = personTaxCode.getName();

        if (!name.isEmpty() && name.get(0).chars().mapToObj(s -> (char) s)
                .map(String::valueOf)
                .filter(s -> !StringUtil.isVowel(s)).count() > 3L) {

            List<String> firstFourConsonants = name.get(0).chars()
                    .mapToObj(s -> (char) s)
                    .map(String::valueOf)
                    .filter(s -> !StringUtil.isVowel(s))
                    .limit(4)
                    .collect(Collectors.toList());

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
