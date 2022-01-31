package it.vodafone.test.dto;

import it.vodafone.test.validator.ValidTaxCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxCode {

    @ValidTaxCode
    private String taxCode;
}
