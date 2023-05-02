package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class UserOrderNotFoundException extends ModuleException{

    public UserOrderNotFoundException(String message) {
        super(message, "40451", HttpStatus.NOT_FOUND);
    }
}
