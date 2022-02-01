package it.vodafone.test.dto;

import it.vodafone.test.enumeration.Gender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class PersonTaxCodeComponents extends GenericPersonTaxCode {

    @NotEmpty(message = "Surname list can't be empty")
    private String surname;
    @NotEmpty(message = "Name list can't be empty")
    private String name;
    private String controlCharacter;

    public PersonTaxCodeComponents(LocalDate birthDate, Gender genderFromTaxCode, String comune
            , Boolean foreignCountry, String surname, String name
            , String controlCharacter) {
        super(birthDate, genderFromTaxCode, comune, foreignCountry);
        this.surname = surname;
        this.name = name;
        this.controlCharacter = controlCharacter;
    }
}
