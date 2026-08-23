package com.hospital.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record AppointmentRequest(

        @NotNull(message = "Patient ID is required")
        @Positive(message = "Patient ID must be positive")
        Long patientId,

        @NotNull(message = "Doctor ID is required")
        @Positive(message = "Doctor ID must be positive")
        Long doctorId,

        @NotNull(message = "Appointment time is required")
        @Future(message = "Appointment time must be in the future")
        LocalDateTime appointmentTime
) {
}