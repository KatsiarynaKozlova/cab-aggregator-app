package com.me.passengerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneAlreadyExistException extends RuntimeException{
    public PhoneAlreadyExistException(String s){
        super(s);
    }
}
