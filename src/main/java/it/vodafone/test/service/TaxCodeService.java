package it.vodafone.test.service;

import it.vodafone.test.dto.*;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.exception.TaxCodeInvalidException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class TaxCodeService {

    private final PersonTaxCodeParser personTaxCodeParser;
    private final PersonTaxCodeCreator personTaxCodeCreator;

    static final String WRONG_LENGTH_MESSAGE = "Wrong taxcode length: %s. The only taxcode length allowed are 9, 11, 16.";

    public AbstractTaxCode generateTaxCodeComponents(TaxCode taxCode) {
        //11 o 16 righe che distinguono persono fisiche dalle partite iva

        if (!asList(9, 11, 16).contains(taxCode.getTaxCode().length())) {
            throw new TaxCodeInvalidException(singletonList(String.format(WRONG_LENGTH_MESSAGE,taxCode.getTaxCode().length())));
        }

        if (taxCode.getTaxCode().length() == 9) {
            return new TemporaryTaxCode(taxCode.getTaxCode());
        }
        if (taxCode.getTaxCode().length() == 11) {
            return new CompanyTaxCode(taxCode.getTaxCode());
        }
        // handling physical person taxcode
        String surname = personTaxCodeParser.getSurname(taxCode.getTaxCode());
        String name = personTaxCodeParser.getName(taxCode.getTaxCode());

        String birthYear = personTaxCodeParser.getCompleteBirthYear(taxCode.getTaxCode());
        Month birthMonth = personTaxCodeParser.getBirthMonth(taxCode.getTaxCode());
        String birthDay = personTaxCodeParser.getBirthDay(taxCode.getTaxCode());

        Gender genderFromTaxCode = Gender.getGenderFromTaxCode(birthDay);
        String comune = personTaxCodeParser.getCountry(taxCode.getTaxCode());
        String controlCharacter = taxCode.getTaxCode().substring(15, 16);

        Boolean foreignCountry = comune.toUpperCase().startsWith("Z");
        LocalDate birthDate = LocalDate.of(Integer.parseInt(birthYear), birthMonth, Integer.parseInt(birthDay));

        return new PhysicalPersonTaxCode(asList(surname), asList(name), birthDate
                , genderFromTaxCode, comune, foreignCountry, controlCharacter);
    }

    public TaxCode taxCodeFromComponents(PhysicalPersonTaxCode taxCode) {
        // case of person taxcode
        return new TaxCode(personTaxCodeCreator.createTaxCode(taxCode));
    }
}
