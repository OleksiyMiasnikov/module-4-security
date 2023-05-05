package com.epam.esm.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ApiEntityNotFoundException extends ApiException {

    public ApiEntityNotFoundException(String message) {
        super(message, "40401", NOT_FOUND);
    }
}
