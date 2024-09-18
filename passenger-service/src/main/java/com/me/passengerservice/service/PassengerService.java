package com.me.passengerservice.service;

import com.me.passengerservice.dto.request.PassengerRequest;
import com.me.passengerservice.dto.response.PassengerListResponse;
import com.me.passengerservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse getPassengerById(Long id);
    PassengerListResponse getAllPassengers();
    PassengerResponse createPassenger(PassengerRequest passengerRequest);
    PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest);
    void deletePassenger(Long id);
}
