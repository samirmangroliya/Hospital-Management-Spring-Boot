package com.hospital.appointment.domain.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hospital.appointment.dto.DoctorInfo;
import com.hospital.appointment.exception.AppointmentException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorGatewayImpl
        implements DoctorGateway {

    private final DoctorFeignClient client;

    @Override
    public DoctorInfo getDoctorById(Long doctorId) {
        try {
            ResponseEntity<DoctorInfo> responseEntity = client.getInternalDoctor(doctorId);
            
            if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
                throw new AppointmentException("Doctor not found with id: " + doctorId);
            }
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new AppointmentException("Error communicating with doctor-service: " + e.getMessage());
        }
    }
}