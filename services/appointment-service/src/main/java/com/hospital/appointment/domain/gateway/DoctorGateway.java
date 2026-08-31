package com.hospital.appointment.domain.gateway;

public interface DoctorGateway {
    boolean checkDoctorExists(Long doctorId);
}