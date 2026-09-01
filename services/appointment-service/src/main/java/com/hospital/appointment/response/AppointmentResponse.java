package com.hospital.appointment.response;

import java.time.LocalDateTime;

import com.hospital.appointment.entity.AppointmentStatus;

public record AppointmentResponse(
        Long id,
        Long patientId,
        Object patientDetails, // Replace 'Object' with your Patient DTO class
        Long doctorId,
        Object doctorDetails,  // Replace 'Object' with your Doctor DTO class
        LocalDateTime appointmentTime,
        AppointmentStatus status,
        LocalDateTime createdAt
) {}