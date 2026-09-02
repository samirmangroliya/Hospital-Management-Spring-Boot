package com.hospital.appointment.domain.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hospital.appointment.dto.PatientInfo;
import com.hospital.appointment.exception.AppointmentException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PatientGatewayImpl
        implements PatientGateway {

    private final PatientFeignClient client;

    @Override
    public PatientInfo getPatientById(Long patientId) {
         try {
            ResponseEntity<PatientInfo> responseEntity = client.getPatientById(patientId);
            
            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                throw new AppointmentException("Patient not found with id: " + patientId);
            }
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new AppointmentException("Error communicating with patient-service: " + e.getMessage());
        }
    }
}