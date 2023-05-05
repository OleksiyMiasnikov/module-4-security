package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class CertificateWithTagNotFoundException extends ApiException {

    public CertificateWithTagNotFoundException(String message) {
        super(message, "40421", HttpStatus.NOT_FOUND);
    }
}
