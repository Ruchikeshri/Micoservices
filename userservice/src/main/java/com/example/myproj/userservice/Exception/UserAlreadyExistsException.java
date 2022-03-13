package com.example.myproj.userservice.Exception;

import org.springframework.web.bind.annotation.ExceptionHandler;


public class UserAlreadyExistsException extends RuntimeException {
    String message;

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        this.message = message;
    }
}
