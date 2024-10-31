package com.software.modsen.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServiceUnAvailableException extends RuntimeException {
    public ServiceUnAvailableException(String message) {
        super(message);
    }
}
