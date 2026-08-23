package com.hospital.appointment.dto;

import com.hospital.appointment.entity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record AppointmentStatusRequest(

        @NotNull(message = "Status is required")
        AppointmentStatus status
) {
}