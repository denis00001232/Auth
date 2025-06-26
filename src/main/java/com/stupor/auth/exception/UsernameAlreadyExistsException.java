package com.stupor.auth.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Пользователь с таким username уже существует");
    }
}
