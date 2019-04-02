package com.example.demo.exception;

public class NotFoundResourceException extends RuntimeException{
    public NotFoundResourceException(String type, Long id){
        super("Could not find " + type + ": id=" + id);
    }
}
