package com.stupor.auth.exception;

public class RefreshException extends RuntimeException {
    public RefreshException() {
        super("Возникла проблема при обновлении токенов");
    }
}
