package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.model.Passenger;

import java.util.List;

public interface PassengerService {
    Passenger getPassengerById(String id);
    List<Passenger> getAllPassengers();
    Passenger createPassenger(Passenger passengerRequest);
    Passenger updatePassenger(String id, Passenger passengerRequest);
    void deletePassenger(String id);
}
