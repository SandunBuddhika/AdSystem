package com.sandun.adSystem.model.exceptions;

public class FailedToLoadAdException extends RuntimeException {
    public FailedToLoadAdException(String message) {
        super(message);
    }
}
