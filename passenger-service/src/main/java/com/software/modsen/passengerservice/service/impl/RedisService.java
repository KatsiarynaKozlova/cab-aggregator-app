package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.model.Passenger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<Long, Passenger> redisTemplate;
    private static final long TIME_TO_LIVE_SECONDS = 300;

    public Passenger getPassengerByIdFromCache(Long key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setPassenger(Long key, Passenger passenger) {
        redisTemplate.opsForValue().set(key, passenger, TIME_TO_LIVE_SECONDS, TimeUnit.SECONDS);
    }

    public void deletePassenger(Long key) {
        redisTemplate.delete(key);
    }
}
