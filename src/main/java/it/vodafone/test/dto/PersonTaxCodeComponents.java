package it.vodafone.test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * Pojo representing a generic model a person tax code
 */
@Setter
@EqualsAndHashCode
public class PersonTaxCodeComponents extends AbstractTaxCode {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private Gender genderFromTaxCode;
    private String country;
    private Boolean foreignCountry = false;
    @NotEmpty(message = "Surname list can't be empty")
    private String surname;
    @NotEmpty(message = "Name list can't be empty")
    private String name;
    private String controlCharacter;

    public PersonTaxCodeComponents(LocalDate birthDate
            , Gender genderFromTaxCode, String country
            , Boolean foreignCountry, String surname
            , String name, String controlCharacter) {
        super(TaxCodeTypeEnum.PHYSICAL_PERSON);
        this.birthDate = birthDate;
        this.genderFromTaxCode = genderFromTaxCode;
        this.country = country;
        this.foreignCountry = foreignCountry;
        this.surname = surname;
        this.name = name;
        this.controlCharacter = controlCharacter;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGenderFromTaxCode() {
        return genderFromTaxCode;
    }

    public void setGenderFromTaxCode(Gender genderFromTaxCode) {
        this.genderFromTaxCode = genderFromTaxCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getForeignCountry() {
        return foreignCountry;
    }

    public void setForeignCountry(Boolean foreignCountry) {
        this.foreignCountry = foreignCountry;
    }
}
