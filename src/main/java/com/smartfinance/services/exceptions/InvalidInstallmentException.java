package com.smartfinance.services.exceptions;

public class InvalidInstallmentException extends RuntimeException {
    public InvalidInstallmentException(String message) {
        super(message);
    }
}
