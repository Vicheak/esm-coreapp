package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class DataFormatException {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(){
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(7777)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors("Failed to deserialize data format!")
                .build();
        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> handleNumberFormatException(NumberFormatException ex){
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(7777)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();
        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateTimeParseException(){
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(7777)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors("Failed to parse the date time format!")
                .build();
        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }


}
