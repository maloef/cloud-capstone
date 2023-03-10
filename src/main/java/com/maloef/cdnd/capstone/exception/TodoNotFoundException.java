package com.maloef.cdnd.capstone.exception;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(String message) {
        super(message);
    }
}
