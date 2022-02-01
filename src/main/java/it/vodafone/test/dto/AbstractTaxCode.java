package it.vodafone.test.dto;

import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class AbstractTaxCode {
    private TaxCodeTypeEnum taxCodeType;

    public AbstractTaxCode(TaxCodeTypeEnum taxCodeType) {
        this.taxCodeType = taxCodeType;
    }
}
