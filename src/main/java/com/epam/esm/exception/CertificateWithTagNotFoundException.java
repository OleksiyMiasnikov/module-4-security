package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class CertificateWithTagNotFoundException extends ModuleException{

    public CertificateWithTagNotFoundException(String message) {
        super(message, "40421", HttpStatus.NOT_FOUND);
    }
}
