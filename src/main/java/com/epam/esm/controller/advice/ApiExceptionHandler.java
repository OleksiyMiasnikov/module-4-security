package com.epam.esm.controller.advice;

import com.epam.esm.exception.ApiErrorResponse;
import com.epam.esm.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception){
        log.warn("An exception '{}' was thrown with code '{}'",
                exception.getApiErrorResponse().getErrorMessage(),
                exception.getApiErrorResponse().getErrorCode());

        return new ResponseEntity<>(exception.getApiErrorResponse(),
                exception.getApiErrorResponse().getStatusCode());
    }

    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleBindException(BindException exception){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode("40001")
                .errorMessage(exception.getMessage())
                .statusCode(BAD_REQUEST)
                .build();

        log.warn("An exception '{}' was thrown with code '{}'",
                apiErrorResponse.getErrorMessage(),
                apiErrorResponse.getErrorCode());

        return new ResponseEntity<>(apiErrorResponse,
                apiErrorResponse.getStatusCode());
    }


    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleTypeMismatchException(TypeMismatchException exception){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode("40002")
                .errorMessage(exception.getMessage())
                .statusCode(BAD_REQUEST)
                .build();

        log.warn("An exception '{}' was thrown with code '{}'",
                apiErrorResponse.getErrorMessage(),
                apiErrorResponse.getErrorCode());

        return new ResponseEntity<>(apiErrorResponse,
                apiErrorResponse.getStatusCode());
    }

    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleHttpMessageConversionException(
            HttpMessageConversionException exception){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode("40003")
                .errorMessage(exception.getMessage())
                .statusCode(BAD_REQUEST)
                .build();

        log.warn("An exception '{}' was thrown with code '{}'",
                apiErrorResponse.getErrorMessage(),
                apiErrorResponse.getErrorCode());

        return new ResponseEntity<>(apiErrorResponse,
                apiErrorResponse.getStatusCode());
    }

}
