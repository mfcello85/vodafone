package it.vodafone.test.dto;

import it.vodafone.test.validator.ValidTaxCode;
import lombok.*;

/**
 * It represents a single taxcode used as the input for the parsing endpoint
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TaxCode {

    @ValidTaxCode
    private String taxCode;
}
