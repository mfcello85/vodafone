package it.vodafone.test.dto;

import it.vodafone.test.enumeration.TaxCodeTypeEnum;

public class AbstractTaxCode {

    private TaxCodeTypeEnum taxCodeType;

    public AbstractTaxCode(TaxCodeTypeEnum taxCodeType) {
        this.taxCodeType = taxCodeType;
    }

}
