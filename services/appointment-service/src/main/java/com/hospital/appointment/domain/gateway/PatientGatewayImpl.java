package com.hospital.appointment.domain.gateway;
 
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PatientGatewayImpl
        implements PatientGateway {

    private final PatientFeignClient client;

    @Override
    public boolean checkPatientExists(Long patientId) {
         return client.checkPatientExists(patientId);
    }
}