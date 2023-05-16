package com.epam.esm.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ApiEntityCouldNotBeDeletedException extends ApiException {

    public ApiEntityCouldNotBeDeletedException(String message) {
        super(message, "50001", INTERNAL_SERVER_ERROR);
    }
}
