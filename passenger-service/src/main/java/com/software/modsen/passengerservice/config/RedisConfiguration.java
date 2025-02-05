package com.software.modsen.passengerservice.config;

import com.software.modsen.passengerservice.model.Passenger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    @Bean
    RedisTemplate<Long, Passenger> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, Passenger> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
