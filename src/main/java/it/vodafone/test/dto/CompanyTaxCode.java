package it.vodafone.test.dto;

import it.vodafone.test.enumeration.TaxCodeTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CompanyTaxCode extends AbstractTaxCode {

    private String taxCode;

    public CompanyTaxCode(String taxCode) {
        super(TaxCodeTypeEnum.COMPANY);
        this.taxCode = taxCode;
    }

}
