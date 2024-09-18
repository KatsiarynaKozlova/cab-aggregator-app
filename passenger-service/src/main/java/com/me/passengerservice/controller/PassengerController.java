package com.me.passengerservice.controller;

import com.me.passengerservice.dto.request.PassengerRequest;
import com.me.passengerservice.dto.response.PassengerListResponse;
import com.me.passengerservice.dto.response.PassengerResponse;
import com.me.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok().body(passengerService.getPassengerById(id));
    }

    @GetMapping
    public ResponseEntity<PassengerListResponse> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.ok().body(passengerService.createPassenger(passengerRequest));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePassenger(@PathVariable Long id){
        passengerService.deletePassenger(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequest passengerRequest
    ) {
        return ResponseEntity.ok().body(passengerService.updatePassenger(id, passengerRequest));
    }
}
