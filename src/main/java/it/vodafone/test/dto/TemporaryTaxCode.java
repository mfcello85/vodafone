package it.vodafone.test.dto;

import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * It represents a temporary taxcode and therefore with length of 9 characters
 */
@Getter
@Setter
@EqualsAndHashCode
public class TemporaryTaxCode extends AbstractTaxCode {

    private String taxCode;

    public TemporaryTaxCode(String taxCode) {
        super(TaxCodeTypeEnum.TEMPORARY);
        this.taxCode = taxCode;
    }
}
