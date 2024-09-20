package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.exception.EmailAlreadyExistException;
import com.software.modsen.passengerservice.exception.PassengerNotFoundException;
import com.software.modsen.passengerservice.exception.PhoneAlreadyExistException;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_NOT_FOUND_EXCEPTION;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_WITH_EMAIL_ALREADY_EXIST_EXCEPTION;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_WITH_PHONE_ALREADY_EXIST_EXCEPTION;

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
