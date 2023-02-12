package com.example.bachelor.utility.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserNotConfirmedException extends UsernameNotFoundException {
    public UserNotConfirmedException (String errorMessage) {
        super(errorMessage);
    }
}
