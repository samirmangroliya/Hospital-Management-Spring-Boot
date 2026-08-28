package com.hospital.doctor.event;

public record DoctorEvent(
        Long doctorId,
        String name,
        String specialization
) {
}