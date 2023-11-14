package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import com.vicheak.coreapp.base.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = new ArrayList<>() {{
            ex.getFieldErrors().forEach(fieldError -> add(FieldError.builder()
                    .fieldName(fieldError.getField())
                    .errorMessage(fieldError.getDefaultMessage())
                    .build()));
        }};

        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(8888)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors(fieldErrors)
                .build();

        return new ResponseEntity<>(baseError, HttpStatus.BAD_REQUEST);
    }

}
