package com.carara.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(httpStatus.value());
        standardError.setError("Resource not found.");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus.value()).body(standardError);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> databaseException(DatabaseException exception, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        StandardError standardError = new StandardError();
        standardError.setTimestamp(Instant.now());
        standardError.setStatus(httpStatus.value());
        standardError.setError("Database exception.");
        standardError.setMessage(exception.getMessage());
        standardError.setPath(request.getRequestURI());

        return ResponseEntity.status(httpStatus.value()).body(standardError);
    }
}
