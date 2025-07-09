package com.wowtkn.backend.exception;

public class WowTokenApiException extends RuntimeException {

    public WowTokenApiException(String message) {
        super(message);
    }

    public WowTokenApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
