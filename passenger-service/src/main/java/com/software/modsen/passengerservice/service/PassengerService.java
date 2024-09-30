package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.model.Passenger;

import java.util.List;

public interface PassengerService {
    Passenger getPassengerById(Long id);
    List<Passenger> getAllPassengers();
    Passenger createPassenger(Passenger passengerRequest);
    Passenger updatePassenger(Long id, Passenger passengerRequest);
    void deletePassenger(Long id);
}
