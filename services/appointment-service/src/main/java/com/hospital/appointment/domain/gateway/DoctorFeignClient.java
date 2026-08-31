package com.hospital.appointment.domain.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service")
public interface DoctorFeignClient {

    @GetMapping("/api/doctors/{id}/exists")
    boolean checkDoctorExists(@PathVariable("id") Long id);
}