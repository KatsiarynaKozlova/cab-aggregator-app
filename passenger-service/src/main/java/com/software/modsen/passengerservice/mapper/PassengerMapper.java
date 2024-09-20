package com.software.modsen.passengerservice.mapper;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.model.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerResponse toPassengerResponse(Passenger passenger);
    Passenger toPassengerEntity(PassengerRequest passengerRequest);
}
