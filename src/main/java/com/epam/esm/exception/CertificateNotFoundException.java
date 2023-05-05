package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class CertificateNotFoundException extends ApiException {

    public CertificateNotFoundException(String message) {
        super(message, "40411", HttpStatus.NOT_FOUND);
    }
}
