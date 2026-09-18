package com.smartfinance.services.exceptions;

public class ResourceNotFoundExcepetion extends RuntimeException {

    public ResourceNotFoundExcepetion(Object id) {
        super("Resource not found. Id " + id);
    }
}
