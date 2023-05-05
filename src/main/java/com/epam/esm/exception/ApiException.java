package com.epam.esm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ApiException extends RuntimeException{

    private ApiErrorResponse apiErrorResponse;

    public ApiException(String message, String errorCode, HttpStatusCode statusCode) {
        super(message);
        this.apiErrorResponse = ApiErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(message)
                .statusCode(statusCode)
                .build();
    }
}
