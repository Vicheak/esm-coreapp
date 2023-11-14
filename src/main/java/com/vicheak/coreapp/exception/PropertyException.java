package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class PropertyException {

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<?> handlePropertyReferenceException(PropertyReferenceException ex){
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(15000)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();
        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }

}
