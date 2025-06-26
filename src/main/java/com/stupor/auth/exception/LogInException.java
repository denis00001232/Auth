package com.stupor.auth.exception;

public class LogInException extends RuntimeException {
    public LogInException() {
        super("Неверные логин или пароль");
    }
}
