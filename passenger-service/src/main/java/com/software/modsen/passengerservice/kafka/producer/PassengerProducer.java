package com.software.modsen.passengerservice.kafka.producer;

import com.software.modsen.passengerservice.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPassengerId(Long id) {
        kafkaTemplate.send(Constants.KAFKA_TOPIC, id);
    }
}
