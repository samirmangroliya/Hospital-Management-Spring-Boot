package com.hospital.doctor.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    public static final String DOCTOR_EVENTS = "doctor.events";

    @Bean
    public NewTopic doctorEventsTopic() {

        return new NewTopic(
                DOCTOR_EVENTS,
                3,
                (short) 1
        );
    }
}