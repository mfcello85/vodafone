package it.vodafone.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTaxCodeParserTest {

    @Mock
    private OmocodeTransformer omocodeTransformer;
    @Mock
    private MonthParser monthParser;

    private static final String TAXCODE = "FLPMCL85H24E704H";

    private PersonTaxCodeParser personTaxCodeParser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        personTaxCodeParser = new PersonTaxCodeParser(omocodeTransformer, monthParser);
    }

    @Test
    public void shouldGetSurname() {
        assertEquals(TAXCODE.substring(0, 3), personTaxCodeParser.getSurname(TAXCODE));
    }

    @Test
    public void shouldGetName() {
        assertEquals(TAXCODE.substring(3, 6), personTaxCodeParser.getName(TAXCODE));
    }

    @Test
    public void shouldGetYear() {
        Mockito.when(omocodeTransformer.cleanFromOmocodeCharacters("85")).thenReturn("85");
        assertEquals(TAXCODE.substring(6, 8), personTaxCodeParser.getYear(TAXCODE));
    }

    @Test
    public void shouldGetCompleteBirthYear() {
        Mockito.when(omocodeTransformer.cleanFromOmocodeCharacters("85")).thenReturn("85");
        assertEquals("1985", personTaxCodeParser.getCompleteBirthYear(TAXCODE));
    }

    @Test
    public void shouldGetCompleteBirthYearWithLessThan() {
        Mockito.when(omocodeTransformer.cleanFromOmocodeCharacters("10")).thenReturn("10");
        String newTaxCode = "FLPMCL10H24E704H";
        assertEquals("2010", personTaxCodeParser.getCompleteBirthYear(newTaxCode));
    }

    @Test
    public void shouldGetRawMonth() {
        assertEquals("H", personTaxCodeParser.getRawMonth(TAXCODE));
    }

    @Test
    public void shouldGetBirthMonth() {
        Mockito.when(monthParser.getMonth("H")).thenReturn(Month.JUNE);
        assertEquals(Month.JUNE, personTaxCodeParser.getBirthMonth(TAXCODE));
    }

    @Test
    public void shouldGetBirthDay() {
        Mockito.when(omocodeTransformer.cleanFromOmocodeCharacters("24")).thenReturn("24");
        assertEquals("24", personTaxCodeParser.getBirthDay(TAXCODE));
    }

    @Test
    public void getCountry() {
        Mockito.when(omocodeTransformer.cleanFromOmocodeCharacters("704")).thenReturn("704");
        assertEquals("E704", personTaxCodeParser.getCountry(TAXCODE));
    }

}
