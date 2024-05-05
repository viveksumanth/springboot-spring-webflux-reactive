package com.springboot.webflux.client.exceptions;

import org.springframework.http.HttpStatusCode;

public class ClientDataException extends RuntimeException {
    public ClientDataException(String message) {
        super(message);
    }
}
