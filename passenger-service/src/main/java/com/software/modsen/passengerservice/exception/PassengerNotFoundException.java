package com.software.modsen.passengerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PassengerNotFoundException extends RuntimeException{
    public PassengerNotFoundException(String s){
        super(s);
    }
}
