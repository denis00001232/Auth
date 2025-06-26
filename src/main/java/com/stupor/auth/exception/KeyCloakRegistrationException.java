package com.stupor.auth.exception;

public class KeyCloakRegistrationException extends RuntimeException {
    public KeyCloakRegistrationException() {
        super("Ошибка регистрации в key cloak");
    }
}
