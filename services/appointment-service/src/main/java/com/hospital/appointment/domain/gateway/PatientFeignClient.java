package com.hospital.appointment.domain.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-service")
public interface PatientFeignClient {

    @GetMapping("/api/patients/{id}")
    Object getPatientById(@PathVariable("id") Long id);
}