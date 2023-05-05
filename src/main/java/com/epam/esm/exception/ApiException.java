package com.epam.esm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiException extends RuntimeException{

    private ApiErrorResponse apiErrorResponse;

    public ApiException(String message, String errorCode, HttpStatusCode httpStatusCode) {
        this.apiErrorResponse.setErrorMessage(message);
        this.apiErrorResponse.setErrorCode(errorCode);
        this.apiErrorResponse.setStatusCode(httpStatusCode);
    }
}
