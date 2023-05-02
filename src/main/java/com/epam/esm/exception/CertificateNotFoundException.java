package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class CertificateNotFoundException extends ModuleException{

    public CertificateNotFoundException(String message) {
        super(message, "40411", HttpStatus.NOT_FOUND);
    }
}
