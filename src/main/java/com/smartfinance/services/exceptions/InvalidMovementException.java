package com.smartfinance.services.exceptions;

public class InvalidMovementException extends RuntimeException {
    public InvalidMovementException(String message) {
        super(message);
    }
}
