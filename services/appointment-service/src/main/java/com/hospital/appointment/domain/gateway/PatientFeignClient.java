package com.hospital.appointment.domain.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.appointment.dto.PatientInfo;

@FeignClient(name = "patient-service")
public interface PatientFeignClient {

    @GetMapping("/api/patients/internal/{id}")
    ResponseEntity<PatientInfo> getPatientById(@PathVariable("id") Long id);
}