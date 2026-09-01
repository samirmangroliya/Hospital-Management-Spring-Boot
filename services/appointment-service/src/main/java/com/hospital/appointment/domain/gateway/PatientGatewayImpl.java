package com.hospital.appointment.domain.gateway;

import org.springframework.stereotype.Component;

import com.hospital.appointment.exception.AppointmentException;
import com.hospital.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PatientGatewayImpl
        implements PatientGateway {

    private final PatientFeignClient client;

    @Override
    public Object getPatientById(Long patientId) {
        Object responseObj = client.getPatientById(patientId);

        if (!(responseObj instanceof ApiResponse<?> apiResponse)) {
            throw new IllegalStateException(
                    "Unexpected response type: " + (responseObj != null ? responseObj.getClass().getName() : "null"));
        }
 
        return apiResponse.getData();
    }
}