package com.hospital.appointment.domain.gateway;

import com.hospital.appointment.dto.DoctorInfo;

public interface DoctorGateway {
    DoctorInfo getDoctorById(Long doctorId);
}