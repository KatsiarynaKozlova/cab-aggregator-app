package com.me.passengerservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassengerRequest {
    private String name;
    private String email;
    private String phone;
}
