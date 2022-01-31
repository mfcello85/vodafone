package it.vodafone.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CinCalculatorTest {

    private String taxCode = "FLPMCL85H24E704";
    private CinCalculator cinCalculator = new CinCalculator();

    @Test
    public void shouldMatchControlCode() {
        String controlCharacter = cinCalculator.getControlCharacter(taxCode);
        Assertions.assertEquals(controlCharacter, "H");
    }


}
