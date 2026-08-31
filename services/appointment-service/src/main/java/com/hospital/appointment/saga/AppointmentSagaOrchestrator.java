package com.hospital.appointment.saga;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.entity.AppointmentStatus;
import com.hospital.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate; // <-- Make sure to import this
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentSagaOrchestrator {

    private final AppointmentRepository appointmentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;             // <-- Added
    private final SagaCoordinationManager sagaCoordinationManager;       // <-- Added

    private static final String TOPIC_NAME = "appointment-saga-topic";

    @KafkaListener(topics = TOPIC_NAME, groupId = "appointment-saga-group")
    @Transactional
    public void handleSagaResponse(SagaEventPayload event) {
        // Ignore initiation events sent by ourselves
        if ("APPOINTMENT_INITIATED".equals(event.step())) {
            return;
        }

        Appointment appointment = appointmentRepository.findById(event.appointmentId()).orElse(null);
        if (appointment == null || appointment.getStatus() != AppointmentStatus.INITIATED) {
            return;
        }
       
        // COMPENSATING ACTION: If either validation fails, cancel the appointment
        if ("PATIENT_FAILED".equals(event.step()) || "DOCTOR_FAILED".equals(event.step())) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);

            // Publish compensation event for cleanup
            SagaEventPayload compensationPayload = new SagaEventPayload(
                    appointment.getId(),
                    appointment.getPatientId(),
                    appointment.getDoctorId(),
                    appointment.getAppointmentTime(),
                    "APPOINTMENT_CANCELLED");
            kafkaTemplate.send(TOPIC_NAME, appointment.getId().toString(), compensationPayload);

            log.warn("Saga Failed: Appointment ID: {} cancelled and compensation event emitted.", appointment.getId());

            // Notify the waiting HTTP thread
            sagaCoordinationManager.complete(appointment.getId(), appointment.getStatus().name());
            return;
        }

        // Track progressive success
        if ("PATIENT_VERIFIED".equals(event.step())) {
            appointment.setPatientVerified(true);
            log.info("Patient verified for appointment ID: {}", appointment.getId());
        }

        if ("DOCTOR_VERIFIED".equals(event.step())) {
            appointment.setDoctorVerified(true);
            log.info("Doctor verified for appointment ID: {}", appointment.getId());
        }

        // If BOTH patient and doctor are successfully verified, complete the saga
        if (appointment.isPatientVerified() && appointment.isDoctorVerified()) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            log.info("Saga Completed Successfully: Appointment ID: {} is now CONFIRMED", appointment.getId());
            
            // Notify the waiting HTTP thread
            sagaCoordinationManager.complete(appointment.getId(), appointment.getStatus().name());
        }

        appointmentRepository.save(appointment);
    }
}