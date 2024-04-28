package com.example.finaldemo.exception;

public class InvalidUserIdException extends RuntimeException{
    public InvalidUserIdException(String message) {
        super(message);
    }
}
