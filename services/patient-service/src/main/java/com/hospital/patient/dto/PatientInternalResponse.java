package com.hospital.patient.dto;

public record PatientInternalResponse(        
        String firstName,
        String lastName,
        String email,
        String phone
) {
}