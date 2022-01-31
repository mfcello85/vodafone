package it.vodafone.test.exception;

import it.vodafone.test.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class BadRquestException extends RuntimeException {

    private final ErrorResponse error;
}
