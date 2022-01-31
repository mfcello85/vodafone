package it.vodafone.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonthParserTest {

    private static MonthParser monthParser = new MonthParser();
    private static Map<String, Month> monthMap = new HashMap<>();

    static {
        monthMap.put("A", Month.JANUARY);
        monthMap.put("B", Month.FEBRUARY);
        monthMap.put("C", Month.MARCH);
        monthMap.put("D", Month.APRIL);
        monthMap.put("E", Month.MAY);
        monthMap.put("H", Month.JUNE);
        monthMap.put("L", Month.JULY);
        monthMap.put("M", Month.AUGUST);
        monthMap.put("P", Month.SEPTEMBER);
        monthMap.put("R", Month.OCTOBER);
        monthMap.put("S", Month.NOVEMBER);
        monthMap.put("T", Month.DECEMBER);
    }

    @BeforeEach
    public void setup() {

    }

    @Test
    public void shouldFailWithInvalidLetter() {
        boolean validMonth = monthParser.isValidMonth("F");
        Assertions.assertFalse(validMonth);
    }

    @ParameterizedTest
    @MethodSource("monthSource")
    public void shouldSucceedWithRightLetter(String character) {
        boolean validMonth = monthParser.isValidMonth(character);
        Assertions.assertTrue(validMonth);
    }

    public static Stream<Arguments> monthSource() {
        return monthParser.getAvailableCharacters()
                .stream()
                .map(Arguments::of);
    }

    @Test
    public void shouldFailWhenLetterNotMatchMonth() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> monthParser.getMonth("F"));
    }

    @Test
    public void shouldSucceedWhenLetterMatchMonth() {
        Month month = Assertions.assertDoesNotThrow(() -> monthParser.getMonth("A"));
        assertEquals(month, Month.JANUARY);
    }

    @ParameterizedTest
    @MethodSource("monthLetterSource")
    public void shouldMatchMonthWithLetter(Month month) {
        String monthLetter = monthParser.getMonthLetter(month);
        assertEquals(month, monthMap.get(monthLetter));
    }

    public static Stream<Arguments> monthLetterSource() {
        return Arrays.stream(Month.values()).map(Arguments::of);
    }


}
