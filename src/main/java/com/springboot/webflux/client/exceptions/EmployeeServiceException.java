package com.springboot.webflux.client.exceptions;

public class EmployeeServiceException extends RuntimeException {
    public EmployeeServiceException(String message) {
        super(message);
    }
}
