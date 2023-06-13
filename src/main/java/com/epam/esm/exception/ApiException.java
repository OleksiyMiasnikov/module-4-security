package com.epam.esm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiException extends RuntimeException{

    private String message;
    private String errorCode;
    private HttpStatusCode status;

    public ApiException(String message, String errorCode, HttpStatusCode status) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }
}
