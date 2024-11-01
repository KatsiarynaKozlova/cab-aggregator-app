package com.software.modsen.authservice.exception.handler;

import com.software.modsen.authservice.exception.InvalidUserDataException;
import com.software.modsen.authservice.exception.ServiceUnAvailableException;
import com.software.modsen.authservice.exception.UserAlreadyExistException;
import com.software.modsen.authservice.exception.WrongCredentialsException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> emailExistException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler({InvalidUserDataException.class, ServiceUnAvailableException.class})
    public ResponseEntity<ErrorMessage> handleBadRequestException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<ErrorMessage> handleUnauthorizedException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
