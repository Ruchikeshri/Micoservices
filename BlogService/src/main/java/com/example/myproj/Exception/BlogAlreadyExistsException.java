package com.example.myproj.Exception;

public class BlogAlreadyExistsException extends RuntimeException {

    private String message;

    public BlogAlreadyExistsException()
    {
    }
    public BlogAlreadyExistsException(String message){
        this.message=message;
    }
}
