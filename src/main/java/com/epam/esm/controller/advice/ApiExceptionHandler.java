package com.epam.esm.controller.advice;

import com.epam.esm.exception.ApiErrorResponse;
import com.epam.esm.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {
    private final ModelMapper mapper = new ModelMapper();

    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception){
        log.warn("An exception '{}' was thrown with errorCode '{}'",
                exception.getMessage(),
                exception.getErrorCode());

        return new ResponseEntity<>(mapper.map(exception, ApiErrorResponse.class),
                exception.getStatus());
    }

    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleBindException(BindException exception){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode("40001")
                .message(Objects.requireNonNull(exception.getFieldError()).getDefaultMessage())
                .status(BAD_REQUEST)
                .build();

        log.warn("An exception '{}' was thrown with errorCode '{}'",
                apiErrorResponse.getMessage(),
                apiErrorResponse.getErrorCode());

        return new ResponseEntity<>(apiErrorResponse,
                apiErrorResponse.getStatus());
    }


    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleTypeMismatchException(TypeMismatchException exception){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode("40002")
                .message(exception.getMessage())
                .status(BAD_REQUEST)
                .build();

        log.warn("An exception '{}' was thrown with errorCode '{}'",
                apiErrorResponse.getMessage(),
                apiErrorResponse.getErrorCode());

        return new ResponseEntity<>(apiErrorResponse,
                apiErrorResponse.getStatus());
    }

    @ExceptionHandler
    private ResponseEntity<ApiErrorResponse> handleHttpMessageConversionException(
            HttpMessageConversionException exception){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode("40003")
                .message(exception.getMessage())
                .status(BAD_REQUEST)
                .build();

        log.warn("An exception '{}' was thrown with errorCode '{}'",
                apiErrorResponse.getMessage(),
                apiErrorResponse.getErrorCode());

        return new ResponseEntity<>(apiErrorResponse,
                apiErrorResponse.getStatus());
    }

}
