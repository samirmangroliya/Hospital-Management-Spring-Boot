package com.hospital.appointment.dto;

public record DoctorInfo(         
        String firstName,
        String lastName,
        String email,
        String phone,
        String specialization
) {
}