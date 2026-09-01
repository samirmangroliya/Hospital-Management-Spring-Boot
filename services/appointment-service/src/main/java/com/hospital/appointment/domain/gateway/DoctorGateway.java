package com.hospital.appointment.domain.gateway;

import com.hospital.appointment.dto.DoctorResponseDto;

public interface DoctorGateway {
    DoctorResponseDto getDoctorById(Long doctorId);
}