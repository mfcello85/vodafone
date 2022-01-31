package it.vodafone.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OmocodeTransformerTest {

    private final static Map<String, Integer> omocodeSource = new HashMap<>();

    OmocodeTransformer omocodeTransformer = new OmocodeTransformer();

    static {
        omocodeSource.put("L", 0);
        omocodeSource.put("M", 1);
        omocodeSource.put("N", 2);
        omocodeSource.put("P", 3);
        omocodeSource.put("Q", 4);
        omocodeSource.put("S", 6);
        omocodeSource.put("T", 7);
        omocodeSource.put("U", 8);
        omocodeSource.put("V", 9);
    }

    @Test
    public void shouldFailWithInvalidOmocode() {
        Assertions.assertFalse(omocodeTransformer.checkValidOmocode("A"));
    }

    @ParameterizedTest
    @MethodSource("omocodeSource")
    public void shouldSucceedWithCorrectOmocode(String omocode) {
        Assertions.assertFalse(omocodeTransformer.checkValidOmocode("A"));
    }

    public static Stream<Arguments> omocodeSource() {
        return omocodeSource.keySet().stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("validInputCharacterSource")
    public void shouldGetCleanCodeCharacter(String inputCharacter, String expectedCharacter) {
        String cleanTaxCodeCharacter = omocodeTransformer.getCleanTaxCodeCharacter(inputCharacter);
        Assertions.assertEquals(cleanTaxCodeCharacter, expectedCharacter);
    }

    public static Stream<Arguments> validInputCharacterSource() {
        return omocodeSource.entrySet().stream().map(s -> Arguments.of(s.getKey(), String.valueOf(s.getValue())));
    }

    @ParameterizedTest
    @MethodSource("invalidInputCharacterSource")
    public void shouldNotChangeNotOmocodeCharacters(String inputCharacter) {
        String cleanTaxCodeCharacter = omocodeTransformer.getCleanTaxCodeCharacter(inputCharacter);
        Assertions.assertEquals(cleanTaxCodeCharacter, inputCharacter);
    }

    public static Stream<Arguments> invalidInputCharacterSource() {
        return IntStream.concat(
                IntStream.rangeClosed('0', '9'),
                IntStream.rangeClosed('A', 'Z')
        ).mapToObj(c -> (char) c).map(String::valueOf)
                .filter(s -> omocodeSource.get(s) == null)
                .map(Arguments::of);
    }

    @Test
    public void shouldCleanFromOmocodeCharacters() {
        String test = "AEIOULMNP";

        String result = omocodeTransformer.cleanFromOmocodeCharacters(test);
        Assertions.assertEquals(result, "AEIO80123");
    }

}
