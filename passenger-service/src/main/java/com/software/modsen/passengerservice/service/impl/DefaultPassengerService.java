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
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION;

@Service
@RequiredArgsConstructor
public class DefaultPassengerService implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerProducer passengerProducer;

    @Override
    public Passenger getPassengerById(Long id) {
        return getByIdOrElseThrow(id);
    }

    @Override
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    @Override
    public Passenger createPassenger(Passenger passengerRequest) {
        validatePassengerCreate(passengerRequest);
        Passenger passenger = passengerRepository.save(passengerRequest);
        PassengerForRating passengerForRating = new PassengerForRating(passenger.getPassengerId());
        passengerProducer.sendPassengerId(passengerForRating);
        return passenger;
    }

    @Override
    public Passenger updatePassenger(Long id, Passenger passenger) {
        Passenger passengerOptional = getByIdOrElseThrow(id);
        validatePassengerUpdate(passenger, passengerOptional);
        passenger.setPassengerId(id);
        try {
            return passengerRepository.save(passenger);
        } catch(OptimisticLockException e){
            throw new PassengerUpdateLockException(ExceptionMessages.TRY_AGAIN_LATER);
        }
    }

    @Override
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    private Passenger getByIdOrElseThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException(String.format(PASSENGER_NOT_FOUND_EXCEPTION, id)));
    }

    private void checkEmailExists(String email) {
        if (passengerRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistException(String.format(PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION, email));
        }
    }

    private void checkPhoneExists(String phone) {
        if (passengerRepository.existsByPhone(phone))
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
