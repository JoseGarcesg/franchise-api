package com.jose.franchiseapi.config;

import com.jose.franchiseapi.interfaces.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex) {

        ErrorResponse error = new ErrorResponse(ex.getReason());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(error);
    }
}
