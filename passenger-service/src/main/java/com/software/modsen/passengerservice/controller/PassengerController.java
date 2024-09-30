package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        Passenger passenger = passengerService.getPassengerById(id);
        PassengerResponse passengerResponse = passengerMapper.toPassengerResponse(passenger);
        return ResponseEntity.ok(passengerResponse);
    }

    @GetMapping
    public ResponseEntity<PassengerListResponse> getAllPassengers() {
        List<Passenger> passengerList =  passengerService.getAllPassengers();
        List<PassengerResponse> passengerResponseList = passengerMapper.toPassengerResponseList(passengerList);
        return ResponseEntity.ok(new PassengerListResponse(passengerResponseList));
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        Passenger newPassenger = passengerMapper.toPassengerEntity(passengerRequest);
        Passenger passenger = passengerService.createPassenger(newPassenger);
        PassengerResponse passengerResponse = passengerMapper.toPassengerResponse(passenger);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(passengerResponse);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerRequest
    ) {
        Passenger newPassenger = passengerMapper.toPassengerEntity(passengerRequest);
        Passenger updatedPassenger = passengerService.updatePassenger(id, newPassenger);
        PassengerResponse passengerResponse = passengerMapper.toPassengerResponse(updatedPassenger);
        return ResponseEntity.ok(passengerResponse);
    }
}
