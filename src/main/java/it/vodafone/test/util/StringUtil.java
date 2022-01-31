package it.vodafone.test.util;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class StringUtil {

    private static Set<String> vowels = Stream.of("A", "E", "I", "O", "U", "a", "e", "i", "o", "u")
            .collect(toSet());

    public static boolean isVowel(String letter) {
        return vowels.contains(letter);
    }
}
