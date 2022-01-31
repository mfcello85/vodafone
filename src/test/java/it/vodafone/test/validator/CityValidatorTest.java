package it.vodafone.test.validator;

import it.vodafone.test.entity.City;
import it.vodafone.test.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CityValidatorTest {

    private CityValidator cityValidator;

    private static final String CITY_NAME = "CITY";
    @Mock
    private CityRepository cityRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        cityValidator = new CityValidator(cityRepository);

        Mockito.when(cityRepository.findByName(CITY_NAME)).thenReturn(Optional.of(new City()));
    }

    @Test
    public void shouldValidateCityWithSuccess() {
        boolean valid = cityValidator.isValid(CITY_NAME, null);
        Assertions.assertTrue(valid);
    }

    @Test
    public void shouldFailToValidateCity() {
        boolean valid = cityValidator.isValid("MILANO", null);
        Assertions.assertFalse(valid);
    }

}
