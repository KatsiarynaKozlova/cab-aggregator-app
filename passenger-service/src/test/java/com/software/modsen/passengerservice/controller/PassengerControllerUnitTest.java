package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.service.impl.DefaultPassengerService;
import com.software.modsen.passengerservice.util.PassengerTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.software.modsen.passengerservice.util.PassengerTestUtil.DEFAULT_PASSENGER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PassengerControllerUnitTest {
    @Mock
    private DefaultPassengerService passengerService;
    @Mock
    private PassengerMapper passengerMapper;
    @InjectMocks
    private PassengerController passengerController;

    @Test
    public void testGetAllPassengers() {
        List<Passenger> passengerList = List.of(PassengerTestUtil.getDefaultPassenger());
        when(passengerService.getAllPassengers()).thenReturn(passengerList);

        ResponseEntity<PassengerListResponse> response = passengerController.getAllPassengers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passengerService, times(1)).getAllPassengers();
    }

    @Test
    public void testGetPassengerById() {
        Passenger passenger = PassengerTestUtil.getDefaultPassenger();
        PassengerResponse passengerResponse = PassengerTestUtil.getDefaultPassengerResponse();
        when(passengerService.getPassengerById(anyLong())).thenReturn(passenger);
        when(passengerMapper.toPassengerResponse(any(Passenger.class))).thenReturn(passengerResponse);

        ResponseEntity<PassengerResponse> resultResponse = passengerController.getPassengerById(DEFAULT_PASSENGER_ID);

        verify(passengerService, times(1)).getPassengerById(DEFAULT_PASSENGER_ID);
        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertEquals(passengerResponse, resultResponse.getBody());
    }

    @Test
    public void testCreatePassenger() {
        Passenger passenger = PassengerTestUtil.getDefaultPreCreatePassenger();
        Passenger createdPassenger = PassengerTestUtil.getDefaultPassenger();
        PassengerRequest passengerRequest = PassengerTestUtil.getDefaultPassengerRequest();
        PassengerResponse expectedPassengerResponse = PassengerTestUtil.getDefaultPassengerResponse();

        when(passengerMapper.toPassengerEntity(any(PassengerRequest.class))).thenReturn(passenger);
        when(passengerService.createPassenger(any(Passenger.class))).thenReturn(createdPassenger);
        when(passengerMapper.toPassengerResponse(any(Passenger.class))).thenReturn(expectedPassengerResponse);

        ResponseEntity<PassengerResponse> resultResponse = passengerController.createPassenger(passengerRequest);

        assertEquals(HttpStatus.CREATED, resultResponse.getStatusCode());
        assertEquals(expectedPassengerResponse, resultResponse.getBody());
        verify(passengerService, times(1)).createPassenger(passenger);
    }

    @Test
    public void testUpdatePassenger() {
        Passenger updatedPassenger = PassengerTestUtil.getDefaultUpdatedPassenger();
        Passenger passenger = PassengerTestUtil.getDefaultPreUpdatePassenger();
        PassengerRequest passengerRequest = PassengerTestUtil.getDefaultUpdatePassengerRequest();
        PassengerResponse expectedPassengerResponse = PassengerTestUtil.getDefaultUpdatedPassengerResponse();

        when(passengerMapper.toPassengerEntity(any(PassengerRequest.class))).thenReturn(passenger);
        when(passengerService.updatePassenger(anyLong(), any(Passenger.class))).thenReturn(updatedPassenger);
        when(passengerMapper.toPassengerResponse(any(Passenger.class))).thenReturn(expectedPassengerResponse);

        ResponseEntity<PassengerResponse> resultResponse = passengerController.updatePassenger(DEFAULT_PASSENGER_ID, passengerRequest);

        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertEquals(expectedPassengerResponse, resultResponse.getBody());
        verify(passengerService, times(1)).updatePassenger(DEFAULT_PASSENGER_ID, passenger);
    }
}
