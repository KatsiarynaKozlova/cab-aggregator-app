package com.software.modsen.passengerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PassengerUpdateLockException extends RuntimeException {
    public PassengerUpdateLockException(String message) {
        super(message);
    }
}
