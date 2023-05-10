package com.epam.esm.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class RefreshTokenFailException extends ApiException {
    public RefreshTokenFailException(String message) {
        super(message, "40111", UNAUTHORIZED);
    }
}
