package it.vodafone.test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PhysicalPersonTaxCode extends AbstractTaxCode {

    @NotEmpty(message = "Surname list can't be empty")
    private List<String> surname;
    @NotEmpty(message = "Name list can't be empty")
    private List<String> name;
    @NotNull(message = "Birth day can't be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @NotNull(message = "Gender can't be null")
    private Gender genderFromTaxCode;
    @NotBlank(message = "Country can't be null")
    private String comune;
    private Boolean foreignCountry = false;
    private String controlCharacter;

    public PhysicalPersonTaxCode(List<String> surname
            , List<String> name, LocalDate birthDate
            , Gender genderFromTaxCode, String comune, Boolean foreignCountry, String controlCharacter) {
        super(TaxCodeTypeEnum.PHYSICAL_PERSON);
        this.surname = surname;
        this.name = name;
        this.birthDate = birthDate;
        this.genderFromTaxCode = genderFromTaxCode;
        this.comune = comune;
        this.foreignCountry = foreignCountry;
        this.controlCharacter = controlCharacter;
    }


}
