package com.stupor.auth.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Пользователь с таким email уже существует");
    }
}
