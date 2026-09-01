package com.hospital.appointment.domain.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.appointment.dto.DoctorResponseDto;

@FeignClient(name = "doctor-service")
public interface DoctorFeignClient {

    @GetMapping("/api/doctors/{id}")
    DoctorResponseDto getDoctorById(@PathVariable("id") Long id);
}