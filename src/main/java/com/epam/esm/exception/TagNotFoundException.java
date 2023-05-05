package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class TagNotFoundException extends ApiException {

    public TagNotFoundException(String message) {
        super(message, "40431", HttpStatus.NOT_FOUND);
    }
}
