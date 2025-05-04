package org.savchenko.auth.exception;

public class RecoverPasswordException extends RuntimeException {
    public RecoverPasswordException() {
        super("Ошибка сброса пароля");
    }
}
