package it.vodafone.test.service;

import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Service
public class MonthParser {

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

    /**
     * Checks is a letter is a valid rappresentation a month according to the conversion map
     * @param letter
     * @return
     */
    public boolean isValidMonth(String letter) {
        return monthMap.containsKey(letter.toUpperCase());
    }

    /**
     * It gets the month representation given a letter
     * @param letter
     * @return
     */
    public Month getMonth(String letter) {
        if (!isValidMonth(letter)) {
            throw new IllegalArgumentException("Monthes are described with letters belonging " +
                    "to the following list: (" + monthMap.keySet()
                    .stream()
                    .collect(joining(",")) + " )");
        }

        return monthMap.get(letter);
    }

    public Set<String> getAvailableCharacters() {
        return monthMap.keySet();
    }

    public String getMonthLetter(Month month) {
        return monthMap.entrySet()
                .stream()
                .filter(s -> s.getValue().equals(month))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
