package com.springboot.webflux.client.controller;

import com.springboot.webflux.client.dto.EmployeeErrorResponse;
import com.springboot.webflux.client.exceptions.ClientDataException;
import com.springboot.webflux.client.exceptions.EmployeeServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmployeeControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(ClientDataException ex) {
        EmployeeErrorResponse error = new EmployeeErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeServiceException ex) {
        EmployeeErrorResponse error = new EmployeeErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setMessage(ex.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
