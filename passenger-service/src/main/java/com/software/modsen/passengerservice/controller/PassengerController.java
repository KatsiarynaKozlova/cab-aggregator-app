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

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerMapper.toPassengerResponse(passengerService.getPassengerById(id)));
    }

    @GetMapping
    public ResponseEntity<PassengerListResponse> getAllPassengers() {
        return ResponseEntity.ok(new PassengerListResponse(
                passengerService.getAllPassengers()
                        .stream()
                        .map(passengerMapper::toPassengerResponse)
                        .collect(Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        Passenger passenger = passengerMapper.toPassengerEntity(passengerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(passengerMapper.toPassengerResponse(passengerService.createPassenger(passenger)));
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
        Passenger passenger = passengerMapper.toPassengerEntity(passengerRequest);
        return ResponseEntity.ok(passengerMapper.toPassengerResponse(passengerService.updatePassenger(id, passenger)));
    }
}
