package it.vodafone.test.dto;

import it.vodafone.test.validator.ValidTaxCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * It represents a single taxcode used as the input for the parsing endpoint
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxCode {

    @ValidTaxCode
    private String taxCode;
}
