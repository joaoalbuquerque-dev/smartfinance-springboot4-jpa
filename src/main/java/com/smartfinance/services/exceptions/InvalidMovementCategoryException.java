package com.smartfinance.services.exceptions;

public class InvalidMovementCategoryException extends RuntimeException {
    public InvalidMovementCategoryException(String message) {
        super(message);
    }
}
