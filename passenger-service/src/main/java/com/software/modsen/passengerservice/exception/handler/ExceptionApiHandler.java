package com.software.modsen.passengerservice.exception.handler;

import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(PassengerNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler({EmailAlreadyExistException.class, PhoneAlreadyExistException.class})
    public ResponseEntity<ErrorMessage> emailExistException(Exception exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
