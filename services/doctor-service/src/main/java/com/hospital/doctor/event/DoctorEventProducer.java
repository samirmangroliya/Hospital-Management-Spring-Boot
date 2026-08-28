package com.hospital.doctor.event;
 
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.hospital.doctor.config.KafkaTopicConfig;

@Component
@RequiredArgsConstructor
public class DoctorEventProducer {

    private final KafkaTemplate<String, DoctorEvent> kafkaTemplate;

    public void publish(DoctorEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.DOCTOR_EVENTS,
                event.doctorId().toString(),
                event
        );
    }
}