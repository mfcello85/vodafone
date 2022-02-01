package it.vodafone.test.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.IntStream.rangeClosed;

@Service
public class CinCalculator {

    private static Map<String, Integer> oddCharacterValues = new HashMap<>();
    private static Map<String, Integer> evenCharacterValues = new HashMap<>();
    private static Map<Integer, String> restConversionValues = new HashMap<>();

    static {
        oddCharacterValues.put("0", 1);
        oddCharacterValues.put("1", 0);
        oddCharacterValues.put("2", 5);
        oddCharacterValues.put("3", 7);
        oddCharacterValues.put("4", 9);
        oddCharacterValues.put("5", 13);
        oddCharacterValues.put("6", 15);
        oddCharacterValues.put("7", 17);
        oddCharacterValues.put("8", 19);
        oddCharacterValues.put("9", 21);
        oddCharacterValues.put("A", 1);
        oddCharacterValues.put("B", 0);
        oddCharacterValues.put("C", 5);
        oddCharacterValues.put("D", 7);
        oddCharacterValues.put("E", 9);
        oddCharacterValues.put("F", 13);
        oddCharacterValues.put("G", 15);
        oddCharacterValues.put("H", 17);
        oddCharacterValues.put("I", 19);
        oddCharacterValues.put("J", 21);
        oddCharacterValues.put("K", 2);
        oddCharacterValues.put("L", 4);
        oddCharacterValues.put("M", 18);
        oddCharacterValues.put("N", 20);
        oddCharacterValues.put("O", 11);
        oddCharacterValues.put("P", 3);
        oddCharacterValues.put("Q", 6);
        oddCharacterValues.put("R", 8);
        oddCharacterValues.put("S", 12);
        oddCharacterValues.put("T", 14);
        oddCharacterValues.put("U", 16);
        oddCharacterValues.put("V", 10);
        oddCharacterValues.put("W", 22);
        oddCharacterValues.put("X", 25);
        oddCharacterValues.put("Y", 24);
        oddCharacterValues.put("Z", 10);

        evenCharacterValues.put("0", 0);
        evenCharacterValues.put("1", 1);
        evenCharacterValues.put("2", 2);
        evenCharacterValues.put("3", 3);
        evenCharacterValues.put("4", 4);
        evenCharacterValues.put("5", 5);
        evenCharacterValues.put("6", 6);
        evenCharacterValues.put("7", 7);
        evenCharacterValues.put("8", 8);
        evenCharacterValues.put("9", 9);
        evenCharacterValues.put("A", 0);
        evenCharacterValues.put("B", 1);
        evenCharacterValues.put("C", 2);
        evenCharacterValues.put("D", 3);
        evenCharacterValues.put("E", 4);
        evenCharacterValues.put("F", 5);
        evenCharacterValues.put("G", 6);
        evenCharacterValues.put("H", 7);
        evenCharacterValues.put("I", 8);
        evenCharacterValues.put("J", 9);
        evenCharacterValues.put("K", 10);
        evenCharacterValues.put("L", 11);
        evenCharacterValues.put("M", 12);
        evenCharacterValues.put("N", 13);
        evenCharacterValues.put("O", 14);
        evenCharacterValues.put("P", 15);
        evenCharacterValues.put("Q", 16);
        evenCharacterValues.put("R", 17);
        evenCharacterValues.put("S", 18);
        evenCharacterValues.put("T", 19);
        evenCharacterValues.put("U", 20);
        evenCharacterValues.put("V", 21);
        evenCharacterValues.put("W", 22);
        evenCharacterValues.put("X", 23);
        evenCharacterValues.put("Y", 24);
        evenCharacterValues.put("Z", 15);

        restConversionValues.put(0, "A");
        restConversionValues.put(1, "B");
        restConversionValues.put(2, "C");
        restConversionValues.put(3, "D");
        restConversionValues.put(4, "E");
        restConversionValues.put(5, "F");
        restConversionValues.put(6, "G");
        restConversionValues.put(7, "H");
        restConversionValues.put(8, "I");
        restConversionValues.put(9, "J");
        restConversionValues.put(10, "K");
        restConversionValues.put(11, "L");
        restConversionValues.put(12, "M");
        restConversionValues.put(13, "N");
        restConversionValues.put(14, "O");
        restConversionValues.put(15, "P");
        restConversionValues.put(16, "Q");
        restConversionValues.put(17, "R");
        restConversionValues.put(18, "S");
        restConversionValues.put(19, "T");
        restConversionValues.put(20, "U");
        restConversionValues.put(21, "V");
        restConversionValues.put(22, "W");
        restConversionValues.put(23, "X");
        restConversionValues.put(24, "Y");
        restConversionValues.put(25, "Z");
    }

    /**
     * Calculates the control character from the given tax code according to the conversion tables.
     * Even and odd characters are converted to numbers and then summed. The rest of the division by 26 is the
     * control character
     * @param taxCode
     * @return the control character of the tax code
     */
    public String getControlCharacter(String taxCode) {

        int controlValueBase = rangeClosed(1, taxCode.length())
                .boxed()
                .map(i -> getCalculatedValue(taxCode, i))
                .mapToInt(i -> i)
                .sum();
        Integer rest = controlValueBase % 26;
        return restConversionValues.get(rest);
    }

    /**
     * Translates the character of the taxcode to the matching number that will be used
     * to calculate the control character
     * @param taxCode
     * @param i
     * @return the numeric translated value of the taxcode characters
     */
    private Integer getCalculatedValue(String taxCode, Integer i) {
        String precalculatedTaxCodeValue = String.valueOf(taxCode.charAt(i - 1));
        if (i % 2 == 0) {
            return evenCharacterValues.get(precalculatedTaxCodeValue.toUpperCase());
        }
        return oddCharacterValues.get(precalculatedTaxCodeValue.toUpperCase());
    }
}
