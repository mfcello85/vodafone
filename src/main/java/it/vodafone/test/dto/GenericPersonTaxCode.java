package it.vodafone.test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import it.vodafone.test.validator.ExistingCity;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@EqualsAndHashCode
public class GenericPersonTaxCode extends AbstractTaxCode {

    private LocalDate birthDate;
    @NotNull(message = "Gender can't be null")
    private Gender genderFromTaxCode;

    @NotBlank(message = "Country can't be null")
    @ExistingCity
    private String comune;
    private Boolean foreignCountry = false;

    public GenericPersonTaxCode(LocalDate birthDate
            , Gender genderFromTaxCode, String comune, Boolean foreignCountry) {
        super(TaxCodeTypeEnum.PHYSICAL_PERSON);
        this.birthDate = birthDate;
        this.genderFromTaxCode = genderFromTaxCode;
        this.comune = comune;
        this.foreignCountry = foreignCountry;
    }

    @NotNull(message = "Birth day can't be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @NotNull(message = "Gender can't be null")
    public Gender getGenderFromTaxCode() {
        return genderFromTaxCode;
    }

    public void setGenderFromTaxCode(Gender genderFromTaxCode) {
        this.genderFromTaxCode = genderFromTaxCode;
    }

    @NotBlank(message = "Country can't be null")
    @ExistingCity
    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public Boolean getForeignCountry() {
        return foreignCountry;
    }

    public void setForeignCountry(Boolean foreignCountry) {
        this.foreignCountry = foreignCountry;
    }
}
