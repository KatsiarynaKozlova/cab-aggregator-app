package com.me.passengerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PassengerResponse {
    private Long passengerId;
    private String name;
    private String email;
    private String phone;
}
