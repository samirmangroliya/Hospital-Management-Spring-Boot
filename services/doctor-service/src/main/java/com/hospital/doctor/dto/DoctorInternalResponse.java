package com.hospital.doctor.dto;

public record DoctorInternalResponse(        
        String firstName,
        String lastName,
        String email,
        String phone,
        String specialization
) {
}