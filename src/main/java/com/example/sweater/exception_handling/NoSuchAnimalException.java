package com.example.sweater.exception_handling;

public class NoSuchAnimalException extends RuntimeException{

    public NoSuchAnimalException(String message) {
        super(message);
    }
}
