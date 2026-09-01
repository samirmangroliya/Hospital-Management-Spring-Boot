package com.hospital.appointment.domain.gateway;

import org.springframework.stereotype.Component;

import com.hospital.appointment.dto.DoctorResponseDto;
import com.hospital.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorGatewayImpl
        implements DoctorGateway {

    private final DoctorFeignClient client;

    @Override
    public DoctorResponseDto getDoctorById(Long doctorId) {
      Object responseObj = client.getDoctorById(doctorId);

        if (!(responseObj instanceof ApiResponse<?> apiResponse)) {
            throw new IllegalStateException(
                    "Unexpected response type: " + (responseObj != null ? responseObj.getClass().getName() : "null"));
        }
 
        return (DoctorResponseDto) apiResponse.getData();
    }
}