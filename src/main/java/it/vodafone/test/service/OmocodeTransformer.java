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

    public Boolean checkValidOmocode(String inputCharacter) {
        return omocodeSource.containsKey(inputCharacter.toUpperCase());
    }

    public String getCleanTaxCodeCharacter(String inputCharacter) {

        if (!checkValidOmocode(inputCharacter)) {
            return inputCharacter;
        }
        return String.valueOf(omocodeSource.get(inputCharacter.toUpperCase()));
    }

    public String cleanFromOmocodeCharacters(String inputString) {
        return inputString.chars()
                .mapToObj(s -> (char) s)
                .map(String::valueOf)
                .map(this::getCleanTaxCodeCharacter)
                .collect(Collectors.joining(""));
    }


}
