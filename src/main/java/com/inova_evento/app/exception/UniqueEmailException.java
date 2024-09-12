package com.inova_evento.app.exception;

public class UniqueEmailException extends RuntimeException{
    public UniqueEmailException(String message) {
        super(message);
    }
}
