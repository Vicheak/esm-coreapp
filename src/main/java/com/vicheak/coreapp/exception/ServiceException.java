package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ServiceException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex){
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(9999)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors(ex.getReason())
                .build();
        return new ResponseEntity<>(baseError, ex.getStatusCode());
    }

}
