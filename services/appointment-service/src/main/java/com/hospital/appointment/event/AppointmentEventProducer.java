package com.hospital.appointment.event;

import com.hospital.appointment.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentEventProducer {

    private final KafkaTemplate<String, AppointmentCreatedEvent> kafkaTemplate;

    public void publish(AppointmentCreatedEvent event) {

        kafkaTemplate.send(
                KafkaTopicConfig.APPOINTMENT_EVENTS,
                event.appointmentId().toString(),
                event
        );
    }
}