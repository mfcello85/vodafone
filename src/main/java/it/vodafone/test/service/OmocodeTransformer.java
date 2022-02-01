package it.vodafone.test.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OmocodeTransformer {

    private final static Map<String, Integer> omocodeSource = new HashMap<>();

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

    /**
     * It checks whether a letter is a valid replacement used to avoid omocode collision
     * @param inputCharacter
     * @return
     */
    public Boolean checkValidOmocode(String inputCharacter) {
        return omocodeSource.containsKey(inputCharacter.toUpperCase());
    }

    /**
     * It removes omocode from the input character leaving untouched the mismatching characters
     * @param inputCharacter
     * @return
     */
    public String getCleanTaxCodeCharacter(String inputCharacter) {

        if (!checkValidOmocode(inputCharacter)) {
            return inputCharacter;
        }
        return String.valueOf(omocodeSource.get(inputCharacter.toUpperCase()));
    }

    /**
     * It removes "omocode characters" from a string
     * @param inputString
     * @return
     */
    public String cleanFromOmocodeCharacters(String inputString) {
        return inputString.chars()
                .mapToObj(s -> (char) s)
                .map(String::valueOf)
                .map(this::getCleanTaxCodeCharacter)
                .collect(Collectors.joining(""));
    }


}
