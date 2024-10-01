package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.model.Passenger;
import com.software.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @GetMapping("/{id}")
    @Operation(description = "Get Passenger by ID ",
            parameters = {@Parameter(name = "id", description = "This is the Passenger ID that will be searched for")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Passenger by ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PassengerResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = @Content(schema = @Schema(hidden = true)))}
    )
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        Passenger passenger = passengerService.getPassengerById(id);
        PassengerResponse passengerResponse = passengerMapper.toPassengerResponse(passenger);
        return ResponseEntity.ok(passengerResponse);
    }

    @GetMapping
    @Operation(description = "Get list of all Passengers ")
    @ApiResponse(responseCode = "200", description = "List of all Passengers",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PassengerListResponse.class))})
    public ResponseEntity<PassengerListResponse> getAllPassengers() {
        List<Passenger> passengerList = passengerService.getAllPassengers();
        List<PassengerResponse> passengerResponseList = passengerMapper.toPassengerResponseList(passengerList);
        return ResponseEntity.ok(new PassengerListResponse(passengerResponseList));
    }

    @PostMapping
    @Operation(description = "Create new Passenger ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PassengerRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Passenger",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PassengerResponse.class))}),
            @ApiResponse(responseCode = "409", description = "Passenger with email/phone already exist",
                    content = @Content(schema = @Schema(hidden = true)))
    })
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
    @Operation(description = "Delete(soft) Passenger bu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete Passenger"),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public void deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update Passenger ",
            parameters = {@Parameter(name = "id", description = "This is the Passenger ID that will be updated")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PassengerRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Passenger",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PassengerResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "Passenger with email/phone already exist",
                    content = @Content(schema = @Schema(hidden = true)))
    })
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
