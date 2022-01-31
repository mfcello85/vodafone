package it.vodafone.test.exception;

import lombok.Data;

@Data
public class NotFoundException extends RuntimeException {
    private final String message;
}