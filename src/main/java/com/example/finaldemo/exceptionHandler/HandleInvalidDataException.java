package com.example.finaldemo.exceptionHandler;

import com.example.finaldemo.exception.InvalidDataException;
import com.example.finaldemo.exception.InvalidUserIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleInvalidDataException extends Exception {
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> HandleInvalidEmailDataException(InvalidDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<String> HandleInvalidUserIdException(InvalidUserIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
