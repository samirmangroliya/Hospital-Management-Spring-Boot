package com.hospital.appointment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hospital.appointment.entity.AppointmentStatus;

import java.time.Instant;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"createdAt", "updatedAt"})
public record AppointmentResponse(

        Long id,

        Long patientId,

        Long doctorId,

        Object patientDetails, // Replace 'Object' with your Patient DTO class

        Object doctorDetails,  // Replace 'Object' with your Doctor DTO class
        
        LocalDateTime appointmentTime,

        AppointmentStatus status,

        Instant createdAt,

        Instant updatedAt
) {
}