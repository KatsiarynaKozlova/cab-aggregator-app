package com.software.modsen.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FeignClientException extends RuntimeException{
    public FeignClientException(String message){
        super(message);
    }
}
