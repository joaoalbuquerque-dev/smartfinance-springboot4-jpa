package com.smartfinance.dto;

public class FieldMessage {

    private String name;
    private String message;

    public FieldMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
