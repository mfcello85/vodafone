package it.vodafone.test.dto;

import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * The person tax code created as the result of the parsing of a single tax code.
 * Therefore name, surname and country will be the respective part of the code
 */
@Getter
@Setter
@EqualsAndHashCode
public class PersonTaxCodeComponents extends GenericPersonTaxCode {

    @NotEmpty(message = "Surname list can't be empty")
    private String surname;
    @NotEmpty(message = "Name list can't be empty")
    private String name;
    private String controlCharacter;

    public PersonTaxCodeComponents(LocalDate birthDate
            , Gender genderFromTaxCode, String comune, Boolean foreignCountry
            , String surname, String name, String controlCharacter) {
        super(TaxCodeTypeEnum.PHYSICAL_PERSON, birthDate, genderFromTaxCode, comune, foreignCountry);
        this.surname = surname;
        this.name = name;
        this.controlCharacter = controlCharacter;
    }
}
