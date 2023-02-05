package com.example.bachelor.utility.exceptions;

public class UserAlreadyExistsException extends IllegalStateException {
    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
