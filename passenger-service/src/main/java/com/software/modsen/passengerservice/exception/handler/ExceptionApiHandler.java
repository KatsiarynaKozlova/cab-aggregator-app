package com.software.modsen.passengerservice.exception.handler;

import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PassengerUpdateLockException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.util.LogInfoMessages;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(PassengerNotFoundException exception) {
        log.info(String.format(LogInfoMessages.NOT_FOUND_EXCEPTION, exception.getMessage()));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler({EmailAlreadyExistException.class, PhoneAlreadyExistException.class, PassengerUpdateLockException.class})
    public ResponseEntity<ErrorMessage> conflictException(Exception exception) {
        log.info(String.format(LogInfoMessages.CONFLICT_EXCEPTION, exception.getMessage()));
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
