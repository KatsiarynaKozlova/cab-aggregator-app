package com.me.passengerservice.mapper;

import com.me.passengerservice.dto.request.PassengerRequest;
import com.me.passengerservice.dto.response.PassengerResponse;
import com.me.passengerservice.model.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    PassengerResponse toPassengerResponse(Passenger passenger);
    Passenger toPassengerEntity(PassengerRequest passengerRequest);
}
