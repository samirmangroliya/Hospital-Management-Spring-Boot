package com.hospital.appointment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(

        @NotNull(message = "Patient ID is required")
        Long patientId,

        @NotNull(message = "Doctor ID is required")
        Long doctorId,

        @NotNull(message = "Appointment time is required")
        @Future(message = "Appointment time must be in the future")
        LocalDateTime appointmentTime
) {
}