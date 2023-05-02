package com.epam.esm.controller.advice;

import com.epam.esm.exception.ModuleErrorResponse;
import com.epam.esm.exception.ModuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ModuleExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ModuleErrorResponse> handleException(ModuleException exception){
        log.warn("An exception '{}' was thrown with code '{}'",
                exception.getMessage(),
                exception.getErrorCode());
        return new ResponseEntity<>(new ModuleErrorResponse(
                exception.getMessage(),
                exception.getErrorCode()),
                exception.getHttpStatusCode());
    }

    @ExceptionHandler
    private ResponseEntity<ModuleErrorResponse> handleException(BindException exception){
        log.warn("An exception '{}' was thrown with code '{}'",
                exception.getMessage(),
                40001);
        return new ResponseEntity<>(new ModuleErrorResponse(
                exception.getMessage(),
                "40001"),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    private ResponseEntity<ModuleErrorResponse> handleException(TypeMismatchException exception){
        log.warn("An exception '{}' was thrown with code '{}'",
                exception.getMessage(),
                40002);
        return new ResponseEntity<>(new ModuleErrorResponse(
                exception.getMessage(),
                "40002"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ModuleErrorResponse> handleException(
            HttpMessageConversionException exception){
        log.warn("An exception '{}' was thrown with code '{}'",
                exception.getMessage(),
                40003);
        return new ResponseEntity<>(new ModuleErrorResponse(
                exception.getMessage(),
                "40003"),
                HttpStatus.BAD_REQUEST);
    }
}
