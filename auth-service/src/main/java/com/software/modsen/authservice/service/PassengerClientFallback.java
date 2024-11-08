package com.software.modsen.authservice.service;

import com.software.modsen.authservice.client.PassengerAuthFeignClient;
import com.software.modsen.authservice.dto.request.FeignUserRequest;
import com.software.modsen.authservice.dto.response.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerClientFallback {
    private final PassengerAuthFeignClient passengerAuthFeignClient;

    @Retry(name = "passengerFeignClient")
    @CircuitBreaker(name = "passengerFeignClient")
    public UserResponse createUser(FeignUserRequest feignUserRequest) {
        return passengerAuthFeignClient.createPassenger(feignUserRequest);
    }

    @Retry(name = "passengerFeignClient")
    @CircuitBreaker(name = "passengerFeignClient")
    public void deleteUser(Long id) {
        passengerAuthFeignClient.deletePassenger(id);
    }
}
