package com.hospital.appointment.domain.gateway;

import com.hospital.appointment.dto.PatientInfo;

public interface PatientGateway {
    PatientInfo getPatientById(Long patientId);
}