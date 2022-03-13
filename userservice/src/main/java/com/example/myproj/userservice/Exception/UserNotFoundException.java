package com.example.myproj.userservice.Exception;

import org.springframework.web.bind.annotation.ExceptionHandler;


public class UserNotFoundException extends RuntimeException {
    String message;

    public UserNotFoundException(String message) {
        this.message = message;
    }


}
