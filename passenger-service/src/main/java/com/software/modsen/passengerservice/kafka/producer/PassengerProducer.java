package com.software.modsen.passengerservice.kafka.producer;

import com.software.modsen.passengerservice.dto.request.PassengerForRating;
import com.software.modsen.passengerservice.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPassengerId(PassengerForRating passenger) {
        Message<PassengerForRating> message = MessageBuilder
                .withPayload(passenger)
                .setHeader(KafkaHeaders.TOPIC, Constants.KAFKA_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
