package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.dto.request.PassengerForRating;
import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PassengerUpdateLockException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.kafka.producer.PassengerProducer;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.PassengerService;
import com.software.modsen.passengerservice.util.ExceptionMessages;
import com.software.modsen.passengerservice.util.LogInfoMessages;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPassengerService implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerProducer passengerProducer;

    @Override
    public Passenger getPassengerById(Long id) {
        Passenger passenger = getByIdOrElseThrow(id);
        log.info(String.format(LogInfoMessages.GET_PASSENGER, id));
        return passenger;
    }

    @Override
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengers = passengerRepository.findAll();
        log.info(LogInfoMessages.GET_LIST_OF_PASSENGERS);
        return passengers;
    }

    @Override
    public Passenger createPassenger(Passenger passengerRequest) {
        validatePassengerCreate(passengerRequest);
        Passenger passenger = passengerRepository.save(passengerRequest);
        log.info(String.format(LogInfoMessages.CREATE_PASSENGER, passenger.getPassengerId()));
        PassengerForRating passengerForRating = new PassengerForRating(passenger.getPassengerId());
        passengerProducer.sendPassengerId(passengerForRating);
        return passenger;
    }

    @Override
    public Passenger updatePassenger(Long id, Passenger passenger) {
        Passenger passengerOptional = getByIdOrElseThrow(id);
        log.info(String.format(LogInfoMessages.GET_PASSENGER, id));
        validatePassengerUpdate(passenger, passengerOptional);
        passenger.setPassengerId(id);
        try {
            Passenger updatedPassenger = passengerRepository.save(passenger);
            log.info(String.format(LogInfoMessages.UPDATE_PASSENGER, id));
            return passengerRepository.save(passenger);
        } catch (OptimisticLockException e) {
            throw new PassengerUpdateLockException(ExceptionMessages.TRY_AGAIN_LATER);
        }
    }

    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
        log.info(String.format(LogInfoMessages.DELETE_PASSENGER, id));
    }

    private Passenger getByIdOrElseThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND_EXCEPTION, id)));
    }

    private void checkEmailExists(String email) {
        if (passengerRepository.existsByEmail(email)) {
            log.info(String.format(LogInfoMessages.PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION, email));
            throw new EmailAlreadyExistException(String.format(PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION, email));
        }
    }

    private void checkPhoneExists(String phone) {
        if (passengerRepository.existsByPhone(phone))
            log.info(String.format(LogInfoMessages.PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION, phone));
        throw new PhoneAlreadyExistException(String.format(PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION, phone));
    }

    private void validatePassengerUpdate(Passenger request, Passenger passenger) {
        if (!request.getEmail().equals(passenger.getEmail())) {
            checkEmailExists(request.getEmail());
        }
        if (!request.getPhone().equals(passenger.getPhone())) {
            checkPhoneExists(request.getPhone());
        }
    }

    private void validatePassengerCreate(Passenger passengerRequest) {
        checkEmailExists(passengerRequest.getEmail());
        checkPhoneExists(passengerRequest.getPhone());
    }
}
