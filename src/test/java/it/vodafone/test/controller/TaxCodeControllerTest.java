package it.vodafone.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.vodafone.test.TestApplication;
import it.vodafone.test.dto.ErrorResponse;
import it.vodafone.test.dto.PersonTaxCode;
import it.vodafone.test.dto.PersonTaxCodeComponents;
import it.vodafone.test.dto.TaxCode;
import it.vodafone.test.entity.Country;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.repository.CityRepository;
import it.vodafone.test.service.MonthParser;
import it.vodafone.test.service.PersonTaxCodeParser;
import it.vodafone.test.service.TaxCodeService;
import it.vodafone.test.validator.TaxCodeValidator;
import it.vodafone.test.validator.TaxCodeValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;

import static it.vodafone.test.validator.TaxCodeValidator.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
public class TaxCodeControllerTest {

    private final static String NAME = "NAME";
    private final static String SURNAME = "SURNAME";
    private final static String TAXCODE = IntStream
            .range(0, 16)
            .boxed()
            .map(s -> "A")
            .collect(joining());
    private final static String RAW_MONTH = "A";
    public static final String COUNTRY_CODE = "A123";
    public static final String NULL = "null";

    @MockBean
    private TaxCodeService taxCodeService;

    @MockBean
    private TaxCodeValidatorUtil taxCodeValidatorUtil;
    @MockBean
    private PersonTaxCodeParser personTaxCodeParser;
    @MockBean
    private MonthParser monthParser;
    @MockBean
    private CityRepository cityRepository;

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setup() {
        when(taxCodeValidatorUtil.checkStringLength(TAXCODE)).thenReturn(true);
        when(taxCodeValidatorUtil.checkMonth(TAXCODE)).thenReturn(true);
        when(taxCodeValidatorUtil.checkAlphanumericCharacters(TAXCODE)).thenReturn(true);
        when(personTaxCodeParser.getSurname(TAXCODE)).thenReturn(SURNAME);
        when(personTaxCodeParser.getName(TAXCODE)).thenReturn(NAME);
        when(personTaxCodeParser.getYear(TAXCODE)).thenReturn("12");
        when(taxCodeValidatorUtil.checkLitteralCharacters(SURNAME)).thenReturn(true);
        when(taxCodeValidatorUtil.checkLitteralCharacters(NAME)).thenReturn(true);
        when(personTaxCodeParser.getRawMonth(TAXCODE)).thenReturn(RAW_MONTH);
        when(taxCodeValidatorUtil.checkMonth(any())).thenReturn(true);
        when(monthParser.getAvailableCharacters()).thenReturn(new HashSet<>());
        when(personTaxCodeParser.getBirthDay(TAXCODE)).thenReturn("12");
        when(personTaxCodeParser.getCountry(TAXCODE)).thenReturn(COUNTRY_CODE);
        when(taxCodeValidatorUtil.checkCountry(COUNTRY_CODE)).thenReturn(true);
        when(cityRepository.findByName(any())).thenReturn(Optional.of(new Country()));

    }

    public ErrorResponse errorResponse(String errorMessage, String rejectedValue) {
        ErrorResponse error = new ErrorResponse("Validation Failed", asList(errorMessage + ": rejected value " + rejectedValue + ""));
        return error;
    }

    @Test
    public void shouldReturnComponents() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCodeComponents taxCodeComponents = new PersonTaxCodeComponents(LocalDate.of(1985, 6, 24)
                , Gender.MALE
                , "E704H", false, "FLP"
                , "MCL", "H");
        when(taxCodeService.generateTaxCodeComponents(taxCode))
                .thenReturn(taxCodeComponents);
        mockMvc.perform(post("/taxcode/component")
                .content(mapper.writeValueAsString(taxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(taxCodeComponents)));
    }

    @Test
    public void shouldReturnTaxCode() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(taxCode)));
    }

    @Test
    public void shouldFailWithWrongLength() throws Exception {
        when(taxCodeValidatorUtil.checkStringLength(TAXCODE)).thenReturn(false);
        verifyBadRequest(WRONG_LENGTH);
    }

    @Test
    public void shouldFailWithNonAlphanumericCharacters() throws Exception {
        when(taxCodeValidatorUtil.checkAlphanumericCharacters(TAXCODE)).thenReturn(false);
        verifyBadRequest(ALPHANUMERIC_CHARACTER);
    }

    @Test
    public void shouldFailWithNonLitteralCharactersName() throws Exception {
        when(taxCodeValidatorUtil.checkLitteralCharacters(NAME)).thenReturn(false);
        verifyBadRequest(NAME_ERROR);
    }

    @Test
    public void shouldFailWithNonLitteralCharactersSurname() throws Exception {
        when(taxCodeValidatorUtil.checkLitteralCharacters(SURNAME)).thenReturn(false);
        verifyBadRequest(SURNAME_ERROR);
    }

    @Test
    public void shouldFailWithNotNumericYear() throws Exception {
        when(personTaxCodeParser.getYear(TAXCODE)).thenReturn("aa");
        verifyBadRequest(YEAR_ERROR);
    }

    @Test
    public void shouldFailWithNotNumericMonth() throws Exception {
        when(taxCodeValidatorUtil.checkMonth(RAW_MONTH)).thenReturn(false);
        when(monthParser.getAvailableCharacters()).thenReturn(new HashSet<>());
        verifyBadRequest("The tax code month field con contain " +
                "only the literal characters : ");
    }

    @Test
    public void shouldFailWithNotNumericBirthDay() throws Exception {
        when(personTaxCodeParser.getBirthDay(TAXCODE)).thenReturn("Z");
        verifyBadRequest(TAX_CODE_DAY_FIELD_NOT_NUMERIC);
    }

    @Test
    public void shouldFailWithNotWrongBirthDay() throws Exception {
        when(personTaxCodeParser.getBirthDay(TAXCODE)).thenReturn("90");
        verifyBadRequest(TaxCodeValidator.WRONG_BIRTH_DAY);
    }

    @Test
    public void shouldFailWithNotWrongCountry() throws Exception {
        when(taxCodeValidatorUtil.checkCountry(COUNTRY_CODE)).thenReturn(false);
        verifyBadRequest(COUNTRY_CODE_ERROR);
    }

    @Test
    public void shouldFailWithNullBirthDate() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setBirthDate(null);
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("Birth day can't be null", NULL))));
    }

    @Test
    public void shouldFailWithNullGender() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setGenderFromTaxCode(null);
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("Gender can't be null", NULL))));

    }

    @Test
    public void shouldFailWithNullCountry() throws Exception {
        when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setCountry(null);
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("Country can't be null", NULL))));

    }

    @Test
    public void shouldFailWithWrongCountry() throws Exception {
        when(cityRepository.findByName(any())).thenReturn(Optional.empty());
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setCountry("AA");
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("There is no country or city in the database for the selected name", "AA"))));

    }

    @Test
    public void shouldFailWithEmptyName() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setSurname(Collections.emptyList());
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("Surname list can't be empty", "[]"))));
    }

    @Test
    public void shouldFailWithShortName() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setSurname(Collections.singletonList("a"));
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("The list should contain at least a two character name", "[a]"))));
    }


    @Test
    public void shouldFailWithEmptySurname() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setName(Collections.emptyList());
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("Name list can't be empty", "[]"))));

    }

    @Test
    public void shouldFailWithShortSurname() throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCode personTaxCode = getPersonTaxCode();
        personTaxCode.setSurname(Collections.singletonList("a"));
        when(taxCodeService.taxCodeFromComponents(personTaxCode))
                .thenReturn(taxCode);
        mockMvc.perform(post("/taxcode")
                .content(mapper.writeValueAsString(personTaxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse("The list should contain at least a two character surname", "[a]"))));
    }

    private PersonTaxCode getPersonTaxCode() {
        PersonTaxCode personTaxCode = new PersonTaxCode(LocalDate.of(1985, 6, 24), Gender.MALE, "LOVERE", false, asList("FELAPPI")
                , asList("MARCELLO")
        );
        return personTaxCode;
    }

    private void verifyBadRequest(String message) throws Exception {
        TaxCode taxCode = new TaxCode(TAXCODE);
        PersonTaxCodeComponents taxCodeComponents = new PersonTaxCodeComponents(LocalDate.of(1985, 6, 24)
                , Gender.MALE
                , "E704H", false, "FLP"
                , "MCL", "H");
        when(taxCodeService.generateTaxCodeComponents(taxCode))
                .thenReturn(taxCodeComponents);
        mockMvc.perform(post("/taxcode/component")
                .content(mapper.writeValueAsString(taxCode))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json(mapper.writeValueAsString(errorResponse(message, TAXCODE))));

    }

}
