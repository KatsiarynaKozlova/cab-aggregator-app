package com.software.modsen.authservice.service;

import com.software.modsen.authservice.client.DriverAuthFeignClient;
import com.software.modsen.authservice.dto.request.FeignUserRequest;
import com.software.modsen.authservice.dto.response.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverClientFallback {
    private final DriverAuthFeignClient driverAuthFeignClient;
    @Retry(name = "driverFeignClient")
    @CircuitBreaker(name = "driverFeignClient")
    public UserResponse createUser(FeignUserRequest user) {
        return driverAuthFeignClient.createDriver(user);
    }

    @Retry(name = "driverFeignClient")
    @CircuitBreaker(name = "driverFeignClient")
    public void deleteUser(String id) {
        driverAuthFeignClient.deleteDriver(id);
    }
}
