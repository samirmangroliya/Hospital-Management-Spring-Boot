package com.hospital.appointment.event;

import java.time.LocalDateTime;

public record AppointmentCreatedEvent(
        Long appointmentId,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentTime,
        String status
) {
}