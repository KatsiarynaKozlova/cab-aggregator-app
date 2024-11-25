package com.software.modsen.authservice.client;

import com.software.modsen.authservice.config.FeignClientConfiguration;
import com.software.modsen.authservice.dto.request.FeignUserRequest;
import com.software.modsen.authservice.dto.response.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "passenger-service",
        configuration = FeignClientConfiguration.class)
public interface PassengerAuthFeignClient {
    @Retry(name = "passengerFeignClient")
    @CircuitBreaker(name = "passengerFeignClient")
    @PostMapping("/passengers")
    UserResponse createPassenger(@RequestBody FeignUserRequest userRequest);

    @Retry(name = "passengerFeignClient")
    @CircuitBreaker(name = "passengerFeignClient")
    @DeleteMapping("/passengers/{id}")
    void deletePassenger(@PathVariable String id);
}
