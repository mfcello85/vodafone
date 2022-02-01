package it.vodafone.test.dto;

import it.vodafone.test.enumeration.Gender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PersonTaxCode extends GenericPersonTaxCode {

    @NotEmpty(message = "Surname list can't be empty")
    private List<String> surname;
    @NotEmpty(message = "Name list can't be empty")
    private List<String> name;
    private String controlCharacter;

    public PersonTaxCode(LocalDate birthDate, Gender genderFromTaxCode, String comune
            , Boolean foreignCountry, List<String> surname, List<String> name
            , String controlCharacter) {
        super(birthDate, genderFromTaxCode, comune, foreignCountry);
        this.surname = surname;
        this.name = name;
        this.controlCharacter = controlCharacter;
    }
}
