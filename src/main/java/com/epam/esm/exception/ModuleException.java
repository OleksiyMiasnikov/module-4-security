package com.epam.esm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ModuleException extends RuntimeException{
    private String message;
    private String errorCode;
    private HttpStatusCode httpStatusCode;

    public ModuleException(String message, String errorCode, HttpStatusCode httpStatusCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }
}
