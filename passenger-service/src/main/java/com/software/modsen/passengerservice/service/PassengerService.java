package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse getPassengerById(Long id);
    PassengerListResponse getAllPassengers();
    PassengerResponse createPassenger(PassengerRequest passengerRequest);
    PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest);
    void deletePassenger(Long id);
}
