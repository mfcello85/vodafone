package it.vodafone.test.service;

import it.vodafone.test.dto.PersonTaxCode;
import it.vodafone.test.dto.PersonTaxCode;
import it.vodafone.test.entity.City;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PersonTaxCodeCreatorTest {

    private PersonTaxCodeCreator personTaxCodeCreator;

    @Spy
    private MonthParser monthParser = new MonthParser();
    @Mock
    private CityRepository cityRepository;
    @Spy
    private CinCalculator cinCalculator = new CinCalculator();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        personTaxCodeCreator = new PersonTaxCodeCreator(monthParser, cityRepository, cinCalculator);
    }

    @Test
    public void shouldGetBirthDayWithMaleUser() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setBirthDate(LocalDate.of(1985, 06, 1));
        String birthDay = personTaxCodeCreator.extractBirthDay(personTaxCode);

        assertEquals(birthDay, "01");
    }

    @Test
    public void shouldGetBirthDayWithFemaleUser() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setBirthDate(LocalDate.of(1985, 06, 1));
        personTaxCode.setGenderFromTaxCode(Gender.FEMALE);
        String birthDay = personTaxCodeCreator.extractBirthDay(personTaxCode);

        assertEquals(birthDay, "41");
    }

    @Test
    public void shouldGetSurnameSectionWithATwoLetterSurname() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setSurname(asList("AB"));
        String surnameSection = personTaxCodeCreator.getSurnameSection(personTaxCode);

        assertEquals(surnameSection, "BAX");
    }

    @Test
    public void shouldGetSurnameSectionWithConsonantsLessThanThree() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setSurname(asList("AB", "EC"));
        String surnameSection = personTaxCodeCreator.getSurnameSection(personTaxCode);

        assertEquals(surnameSection, "BCA");
    }

    @Test
    public void shouldGetSurnameSectionWithConsonantsMoreThanThree() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setSurname(asList("ABCD", "EC"));
        String surnameSection = personTaxCodeCreator.getSurnameSection(personTaxCode);

        assertEquals(surnameSection, "BCD");
    }

    @Test
    public void shouldGetNameSectionWithATwoLetterName() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setName(asList("AB"));
        String nameSection = personTaxCodeCreator.getNameSection(personTaxCode);

        assertEquals(nameSection, "BAX");
    }

    @Test
    public void shouldGetNameSectionWithMoreThanThreeConsonants() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setName(asList("ABCDFG"));
        String nameSection = personTaxCodeCreator.getNameSection(personTaxCode);

        assertEquals(nameSection, "BDF");
    }

    @Test
    public void shouldGetNameSectionWithThreeConsonants() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setName(asList("TZNAA"));
        String nameSection = personTaxCodeCreator.getNameSection(personTaxCode);

        assertEquals(nameSection, "TZN");
    }

    @Test
    public void shouldGetNameSectionWithLessThanThreeConsonants() {
        PersonTaxCode personTaxCode = generatePersonTaxCode();
        personTaxCode.setName(asList("ABC"));
        String nameSection = personTaxCodeCreator.getNameSection(personTaxCode);

        assertEquals(nameSection, "BCA");
    }

    private PersonTaxCode generatePersonTaxCode() {
        PersonTaxCode personTaxCode = new PersonTaxCode(LocalDate.of(1985, 6, 24), Gender.MALE, "LOVERE", false,asList("FELAPPI")
                , asList("MARCELLO")
                );

        return personTaxCode;
    }

    @Test
    public void shouldCreateTaxCode() {
        City city = new City();
        city.setId(1);
        city.setName("LOVERE");
        city.setCode("E704");

        when(cityRepository.findByName(any())).thenReturn(Optional.of(city));
        PersonTaxCode personTaxCode = generatePersonTaxCode();

        String taxCode = personTaxCodeCreator.createTaxCode(personTaxCode);

        Assertions.assertEquals(taxCode, "FLPMCL85H24E704H");
    }


}
