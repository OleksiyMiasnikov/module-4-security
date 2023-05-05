package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class NonAuthorizedRequestException extends ApiException {
    public NonAuthorizedRequestException(String message) {
        super(message, "40301", HttpStatus.FORBIDDEN);
    }
}
