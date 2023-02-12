package com.example.bachelor.utility.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotActiveException extends UsernameNotFoundException {
    public UserNotActiveException(String errorMessage) {
        super(errorMessage);
    }
}
