package com.hospital.appointment.domain.gateway;
 
public interface PatientGateway {
    boolean checkPatientExists(Long patientId);
}