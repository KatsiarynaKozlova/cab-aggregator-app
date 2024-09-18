package com.me.passengerservice.service.impl;

import com.me.passengerservice.dto.request.PassengerRequest;
import com.me.passengerservice.dto.response.PassengerListResponse;
import com.me.passengerservice.dto.response.PassengerResponse;
import com.me.passengerservice.exception.EmailAlreadyExistException;
import com.me.passengerservice.exception.PassengerNotFoundException;
import com.me.passengerservice.exception.PhoneAlreadyExistException;
import com.me.passengerservice.mapper.PassengerMapper;
import com.me.passengerservice.model.Passenger;
import com.me.passengerservice.repository.PassengerRepository;
import com.me.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.me.passengerservice.util.ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION;
import static com.me.passengerservice.util.ExceptionMessages.PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION;
import static com.me.passengerservice.util.ExceptionMessages.PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION;

@Service
@RequiredArgsConstructor
public class DefaultPassengerService implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerResponse getPassengerById(Long id) {
        return passengerMapper.toPassengerResponse(getByIdOrElseThrow(id));
    }

    @Override
    public PassengerListResponse getAllPassengers() {
        return new PassengerListResponse(passengerRepository.findAll().stream()
                .map(passenger -> passengerMapper.toPassengerResponse(passenger))
                .collect(Collectors.toList()));
    }

    @Override
    public PassengerResponse createPassenger(PassengerRequest passengerRequest) {
        validatePassengerCreate(passengerRequest);
        Passenger passenger = passengerMapper.toPassengerEntity(passengerRequest);
        return passengerMapper.toPassengerResponse(passengerRepository.save(passenger));
    }

    @Override
    public PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest) {
        Passenger passenger_opt = getByIdOrElseThrow(id);
        validatePassengerUpdate(passengerRequest, passenger_opt);
        Passenger passenger = passengerMapper.toPassengerEntity(passengerRequest);
        passenger.setPassengerId(id);
        return passengerMapper.toPassengerResponse(passengerRepository.save(passenger));
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

    private void validatePassengerUpdate(PassengerRequest request, Passenger passenger) {
        if (!request.getEmail().equals(passenger.getEmail())) {
            checkEmailExists(request.getEmail());
        }
        if (!request.getPhone().equals(passenger.getPhone())) {
            checkPhoneExists(request.getPhone());
        }
    }

    private void validatePassengerCreate(PassengerRequest passengerRequest) {
        checkEmailExists(passengerRequest.getEmail());
        checkPhoneExists(passengerRequest.getPhone());
    }
}
