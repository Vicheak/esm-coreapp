package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class SqlException {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleSqlException(){
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(13333)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors("There must be problem with data integrity violation!")
                .build();
        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }

}

