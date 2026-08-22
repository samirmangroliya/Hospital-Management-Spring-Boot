package com.hospital.appointment.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime appointmentTime,
        String status
) {
}