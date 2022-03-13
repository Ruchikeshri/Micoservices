package com.example.myproj.Exception;

public class BlogNotFoundException extends RuntimeException {

    private String message;

    public BlogNotFoundException() {
    }

    public BlogNotFoundException(String message) {
        this.message = message;
    }
}
