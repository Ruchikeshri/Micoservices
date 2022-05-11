package com.example.myproj.Exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value(value="${data.value.message1}")

    private String message1;
//    @Value(value = "BlogNotFound")
    @Value(value = "${data.exception.message2}")
    private  String message2;
    @ExceptionHandler(value =BlogAlreadyExistsException.class)
    public ResponseEntity<String> blogAlreadyExistsException()
    {
        return new ResponseEntity<String>(message1, HttpStatus.CONFLICT);

    }


    @ExceptionHandler(value=BlogNotFoundException.class)
    public ResponseEntity<String> blogNotFoundException()
    {
        return new ResponseEntity<>(message2,HttpStatus.CONFLICT);
    }
}
