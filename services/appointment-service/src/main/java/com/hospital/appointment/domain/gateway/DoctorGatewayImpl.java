package com.hospital.appointment.domain.gateway;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorGatewayImpl
        implements DoctorGateway {

    private final DoctorFeignClient client;

    @Override
    public boolean checkDoctorExists(Long doctorId) {
       return client.checkDoctorExists(doctorId);
    }    
}