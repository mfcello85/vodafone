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

/**
 * Pojo representing a generic model a person tax code
 */
@Setter
@EqualsAndHashCode
public class GenericPersonTaxCode extends AbstractTaxCode {

    private LocalDate birthDate;
    private Gender genderFromTaxCode;
    private String comune;
    private Boolean foreignCountry = false;

    public GenericPersonTaxCode(TaxCodeTypeEnum taxCodeType, LocalDate birthDate
            , Gender genderFromTaxCode, String comune, Boolean foreignCountry) {
        super(taxCodeType);
        this.birthDate = birthDate;
        this.genderFromTaxCode = genderFromTaxCode;
        this.comune = comune;
        this.foreignCountry = foreignCountry;
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
