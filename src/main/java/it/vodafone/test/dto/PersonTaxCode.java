package it.vodafone.test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.validator.ExistingCountry;
import it.vodafone.test.validator.InvalidList;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * The person taxcode components used as input to create a unique taxcode
 */
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode
public class PersonTaxCode {

    @NotNull(message = "Birth day can't be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @NotNull(message = "Gender can't be null")
    private Gender genderFromTaxCode;
    @NotBlank(message = "Country can't be null")
    @ExistingCountry
    private String country;
    private Boolean foreignCountry = false;
    @NotEmpty(message = "Surname list can't be empty")
    @InvalidList(message = "The list should contain at least a two character surname")
    private List<String> surname;
    @NotEmpty(message = "Name list can't be empty")
    @InvalidList(message = "The list should contain at least a two character name")
    private List<String> name;

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

    public List<String> getSurname() {
        return surname;
    }

    public void setSurname(List<String> surname) {
        this.surname = surname;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
