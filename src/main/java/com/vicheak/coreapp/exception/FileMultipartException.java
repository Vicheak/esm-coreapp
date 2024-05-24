package com.vicheak.coreapp.exception;

import com.vicheak.coreapp.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class FileMultipartException {

    @ExceptionHandler({MultipartException.class, MaxUploadSizeExceededException.class})
    public ResponseEntity<?> handleFileMultipartException(Exception ex) {
        var baseError = BaseError.builder()
                .message("Something went wrong!")
                .code(30000)
                .status(false)
                .localDateTime(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();

        return new ResponseEntity<>(baseError,
                ex.getClass().getSimpleName().equals("MaxUploadSizeExceededException") ?
                        HttpStatus.PAYLOAD_TOO_LARGE : HttpStatus.BAD_REQUEST);
    }

}
