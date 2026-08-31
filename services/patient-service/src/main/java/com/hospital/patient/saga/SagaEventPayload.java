package com.hospital.patient.saga;

import java.time.LocalDateTime;

public record SagaEventPayload(
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentTime,
        String step
) {
}