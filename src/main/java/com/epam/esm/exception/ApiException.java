package com.epam.esm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiException extends RuntimeException{

    private String message;
    private String code;
    private HttpStatusCode status;

    public ApiException(String message, String code, HttpStatusCode status) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
