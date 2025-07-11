package com.stupor.auth.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
