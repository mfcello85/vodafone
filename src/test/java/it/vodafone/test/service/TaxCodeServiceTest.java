package it.vodafone.test.service;

import it.vodafone.test.dto.*;
import it.vodafone.test.entity.Country;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.exception.TaxCodeInvalidException;
import it.vodafone.test.repository.CountryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static it.vodafone.test.service.TaxCodeService.WRONG_LENGTH_MESSAGE;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TaxCodeServiceTest {

    @Mock
    private PersonTaxCodeParser personTaxCodeParser;
    @Mock
    private PersonTaxCodeCreator personTaxCodeCreator;
    @Mock
    private CountryRepository countryRepository;
    private TaxCodeService taxCodeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        taxCodeService = new TaxCodeService(personTaxCodeParser
                , personTaxCodeCreator, countryRepository);
    }

    @Test
    public void generateTaxCodeComponentsWithNotAllowedLength() {
        TaxCode taxCode = new TaxCode("AAAAA");
        TaxCodeInvalidException taxCodeInvalidException = assertThrows(TaxCodeInvalidException.class,
                () -> taxCodeService.generateTaxCodeComponents(taxCode));
        taxCodeInvalidException.getError()
                .getDetails()
                .get(0)
                .equals(format(WRONG_LENGTH_MESSAGE, taxCode.getTaxCode().length()));
    }

    @Test
    public void generateTaxCodeComponentsWithTemporaryTaxCode() {
        TaxCode taxCode = new TaxCode("AAAAAAAAA");

        AbstractTaxCode abstractTaxCode = taxCodeService.generateTaxCodeComponents(taxCode);
        assertInstanceOf(TemporaryTaxCode.class, abstractTaxCode);
    }

    @Test
    public void generateTaxCodeComponentsWithCompanyTaxCode() {
        TaxCode taxCode = new TaxCode("AAAAAAAAAAA");

        AbstractTaxCode abstractTaxCode = taxCodeService.generateTaxCodeComponents(taxCode);
        assertInstanceOf(CompanyTaxCode.class, abstractTaxCode);
    }

    @Test
    public void generateTaxCodeComponentsWithGenericTaxCode() {
        Country country = new Country();
        country.setName("LOVERE");
        TaxCode taxCode = new TaxCode("FLPMCL85H24E704H");
        when(personTaxCodeParser.getSurname(taxCode.getTaxCode())).thenReturn("FLP");
        when(personTaxCodeParser.getName(taxCode.getTaxCode())).thenReturn("MCL");
        when(personTaxCodeParser.getCompleteBirthYear(taxCode.getTaxCode())).thenReturn("1985");
        when(personTaxCodeParser.getBirthMonth(taxCode.getTaxCode())).thenReturn(Month.JUNE);
        when(personTaxCodeParser.getBirthDay(taxCode.getTaxCode())).thenReturn("24");
        when(personTaxCodeParser.getCountry(taxCode.getTaxCode())).thenReturn("E704H");
        when(countryRepository.findByCode("E704H")).thenReturn(Optional.of(country));
        AbstractTaxCode abstractTaxCode = taxCodeService.generateTaxCodeComponents(taxCode);
        assertInstanceOf(PersonTaxCodeComponents.class, abstractTaxCode);

        PersonTaxCodeComponents physicalPersonTaxCode = new PersonTaxCodeComponents(LocalDate.of(1985, 6, 24)
                , Gender.MALE
                , "LOVERE", false, "FLP"
                , "MCL", "H");
        Assertions.assertEquals(physicalPersonTaxCode, abstractTaxCode);
    }


}
