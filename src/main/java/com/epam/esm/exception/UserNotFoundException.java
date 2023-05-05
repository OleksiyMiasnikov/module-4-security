package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(String message) {
        super(message, "40441", HttpStatus.NOT_FOUND);
    }
}
