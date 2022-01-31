package it.vodafone.test.validator;

import it.vodafone.test.service.MonthParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;

public class TaxCodeValidationUtilTest {

    @Mock
    private MonthParser monthParser;
    private TaxCodeValidatorUtil taxCodeValidatorUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        taxCodeValidatorUtil = new TaxCodeValidatorUtil(monthParser);
    }

    @ParameterizedTest
    @MethodSource("alphanumericStrings")
    public void shouldValidateAlphanumericString(String input, Boolean expected) {
        Boolean result = taxCodeValidatorUtil.checkAlphanumericCharacters(input);
        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> alphanumericStrings() {
        return Stream.of(Arguments.of("aBa1", true)
                , Arguments.of("??", false));
    }

    @ParameterizedTest
    @MethodSource("litteralStrings")
    public void shouldValidateLiteralString(String input, Boolean expected) {
        Boolean result = taxCodeValidatorUtil.checkLitteralCharacters(input);
        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> litteralStrings() {
        return Stream.of(Arguments.of("aBa", true)
                , Arguments.of("??", false)
                , Arguments.of("12a", false));
    }

    @Test
    public void shouldCheckMonhWithSuccessWhenValid() {
        String monthLetter = "A";
        when(monthParser.isValidMonth(monthLetter)).thenReturn(true);

        boolean result = taxCodeValidatorUtil.checkMonth(monthLetter);
        Assertions.assertTrue(result);
    }

    @Test
    public void checkMonhShouldFailWhenMonthNotFound() {
        String monthLetter = "A";
        when(monthParser.isValidMonth(monthLetter)).thenReturn(false);

        boolean result = taxCodeValidatorUtil.checkMonth(monthLetter);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("countryCodes")
    public void shouldCheckCountry(String countryCode, Boolean expected) {
        boolean result = taxCodeValidatorUtil.checkCountry(countryCode);
        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> countryCodes() {
        return Stream.of(Arguments.of("a123", true)
                , Arguments.of("1234", false)
                , Arguments.of("123a", false));
    }

}
