package com.omon4412.authservice.exception;

public class KafkaMessageException extends RuntimeException {
    public KafkaMessageException(String message) {
        super(message);
    }
}
