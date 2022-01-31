package it.vodafone.test.exception;

import it.vodafone.test.dto.ErrorResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;


@Getter
@EqualsAndHashCode
public class TaxCodeInvalidException extends BadRquestException {

    private static final String TEMPLATE_MESSAGE = "TaxCode invalid for the following reasons:";

    public TaxCodeInvalidException(List<String> messages) {
        super(new ErrorResponse(TEMPLATE_MESSAGE, messages));
     }
}
