package com.omon4412.authservice.exception;

/**
 * Исключение, выбрасываемое, данные неверны.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
