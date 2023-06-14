package com.devsuperio.dscatalog.handler;

import com.devsuperio.dscatalog.exceptions.DatabaseException;
import com.devsuperio.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperio.dscatalog.messages.ApiErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorMessage> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(Instant.now(), HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ApiErrorMessage> database(DatabaseException e, HttpServletRequest request) {
        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(apiErrorMessage, HttpStatus.BAD_REQUEST);
    }
}
