package com.software.modsen.passengerservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PassengerRequest {
    private String name;
    private String email;
    private String phone;
}
