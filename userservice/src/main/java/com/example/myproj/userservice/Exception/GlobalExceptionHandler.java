package com.example.myproj.userservice.Exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value(value="${data.exception.message1}")
    private String message1;
    @ExceptionHandler(value=UserAlreadyExistsException.class)
    public ResponseEntity<String> UserAlreadyExists(UserAlreadyExistsException userAlreadyExistsException)
    {
        return  new ResponseEntity<>(message1, HttpStatus.CONFLICT);
    }
    @Value(value = "${data.exception.message2}")
    private  String message2;
    @ExceptionHandler(value=UserNotFoundException.class)
public ResponseEntity<String> UserNotFound(UserNotFoundException userNotFoundException)
    {
        return new ResponseEntity<String>(message2,HttpStatus.CONFLICT);
    }
}
