package com.epam.esm.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class NonAuthenticateUserException extends ApiException {
    public NonAuthenticateUserException(String message) {
        super(message, "40101", UNAUTHORIZED);
    }
}
