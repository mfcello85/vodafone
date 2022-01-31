package it.vodafone.test.validator;

import it.vodafone.test.service.MonthParser;
import it.vodafone.test.service.PersonTaxCodeParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.stream.IntStream;

import static it.vodafone.test.validator.TaxCodeValidator.*;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaxCodeValidatorTest {

    public static final String COUNTRY_CODE = "A123";
    @Mock
    private TaxCodeValidator taxCodeValidator;
    @Mock
    private PersonTaxCodeParser personTaxCodeParser;
    @Mock
    private TaxCodeValidatorUtil taxCodeValidatorUtil;
    @Mock
    private MonthParser monthParser;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    private final static String NAME = "NAME";
    private final static String SURNAME = "SURNAME";
    private final static String TAXCODE = IntStream
            .range(0, 16)
            .boxed()
            .map(s -> "A")
            .collect(joining());
    private final static String RAW_MONTH = "A";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        taxCodeValidator = new TaxCodeValidator(personTaxCodeParser
                , taxCodeValidatorUtil, monthParser);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(null);
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
    }

    @Test
    public void shouldFailIfWrongLength() {
        when(taxCodeValidatorUtil.checkStringLength(TAXCODE)).thenReturn(false);
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(WRONG_LENGTH);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfNotAlphanumericCharacters() {
        when(taxCodeValidatorUtil.checkAlphanumericCharacters(TAXCODE)).thenReturn(false);
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(ALPHANUMERIC_CHARACTER);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfNotSurnameWithLitteralCharacters() {
        when(taxCodeValidatorUtil.checkLitteralCharacters(SURNAME)).thenReturn(false);
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(SURNAME_ERROR);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfNotNameWithLitteralCharacters() {
        when(taxCodeValidatorUtil.checkLitteralCharacters(NAME)).thenReturn(false);
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(NAME_ERROR);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfNotNumericYear() {
        when(personTaxCodeParser.getYear(TAXCODE)).thenReturn("aa");
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext).buildConstraintViolationWithTemplate(YEAR_ERROR);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfWrongMonthCharacter() {
        when(taxCodeValidatorUtil.checkMonth(RAW_MONTH)).thenReturn(false);
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext)
                .buildConstraintViolationWithTemplate("The tax code month field con contain " +
                        "only the literal characters : ");
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfNotNumericBirthDayCharacter() {
        when(personTaxCodeParser.getBirthDay(TAXCODE)).thenReturn("AA");
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext)
                .buildConstraintViolationWithTemplate(TAX_CODE_DAY_FIELD_NOT_NUMERIC);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfWrongBirthDayCharacter() {
        when(personTaxCodeParser.getBirthDay(TAXCODE)).thenReturn("80");
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext)
                .buildConstraintViolationWithTemplate(WRONG_BIRTH_DAY);
        assertFalse(valid);
    }

    @Test
    public void shouldFailIfWrongCountryCode() {
        when(taxCodeValidatorUtil.checkCountry(COUNTRY_CODE)).thenReturn(false);
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext)
                .buildConstraintViolationWithTemplate(COUNTRY_CODE_ERROR);
        assertFalse(valid);
    }

    @Test
    public void shouldPassWhenAllTheChecksAreGreen() {
        boolean valid = taxCodeValidator.isValid(TAXCODE, constraintValidatorContext);
        verify(constraintValidatorContext, never()).buildConstraintViolationWithTemplate(any());

        assertTrue(valid);
    }

}
