package com.epam.esm.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class NonAuthorizedRequestException extends ApiException {
    public NonAuthorizedRequestException(String message) {
        super(message, "40301", FORBIDDEN);
    }
}
