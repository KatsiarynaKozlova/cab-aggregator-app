package com.software.modsen.passengerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PassengerListResponse {
    private List<PassengerResponse> passengerResponseList;
}
