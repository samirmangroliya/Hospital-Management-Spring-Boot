package com.hospital.appointment.dto;

public record PatientInfo(
        String firstName,
        String lastName,
        String email,
        String phone       
) {
}