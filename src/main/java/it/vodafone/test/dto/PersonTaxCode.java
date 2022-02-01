package it.vodafone.test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.vodafone.test.enumeration.Gender;
import it.vodafone.test.validator.ExistingCity;
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
@Getter
@Setter
public class PersonTaxCode {

    @NotNull(message = "Birth day can't be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @NotNull(message = "Gender can't be null")
    private Gender genderFromTaxCode;
    @NotBlank(message = "Country can't be null")
    @ExistingCity
    private String comune;
    private Boolean foreignCountry = false;
    @NotEmpty(message = "Surname list can't be empty")
    private List<String> surname;
    @NotEmpty(message = "Name list can't be empty")
    private List<String> name;

}
