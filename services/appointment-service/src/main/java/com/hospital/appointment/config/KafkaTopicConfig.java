package com.hospital.appointment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String APPOINTMENT_EVENTS = "appointment.events";

    @Bean
    public NewTopic appointmentEventsTopic() {
        return new NewTopic(
                APPOINTMENT_EVENTS,
                4,
                (short) 1);
    }
}