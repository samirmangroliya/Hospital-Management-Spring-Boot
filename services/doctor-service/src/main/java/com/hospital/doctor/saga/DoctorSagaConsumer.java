package com.hospital.doctor.saga;

import com.hospital.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DoctorSagaConsumer {

    private final DoctorRepository doctorRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_NAME = "appointment-saga-topic";

    @KafkaListener(topics = TOPIC_NAME, groupId = "doctor-saga-group")
    public void consumeAppointmentInitiated(SagaEventPayload event) {
        // Only process if the event is at the initiation step
        if (!"APPOINTMENT_INITIATED".equals(event.step())) {
            return;
        }

        log.info("Received appointment initiation for doctor ID: {}", event.doctorId());

        boolean exists = doctorRepository.existsById(event.doctorId());
        

        String nextStep = exists ? "DOCTOR_VERIFIED" : "DOCTOR_FAILED";

        // Create response payload
        SagaEventPayload responseEvent = new SagaEventPayload(
                event.appointmentId(),
                event.patientId(),
                event.doctorId(),
                event.appointmentTime(),
                nextStep
        );

        // Send response back to Kafka
        kafkaTemplate.send(TOPIC_NAME, event.appointmentId().toString(), responseEvent);
        log.info("Sent saga response step: {} for appointment ID: {}", nextStep, event.appointmentId());
    }
}