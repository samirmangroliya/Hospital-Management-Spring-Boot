package com.hospital.appointment.domain.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.appointment.dto.DoctorInfo;
 
@FeignClient(name = "doctor-service")
public interface DoctorFeignClient {

    @GetMapping("/api/doctors/internal/{id}")
    ResponseEntity<DoctorInfo> getInternalDoctor(
            @PathVariable("id") Long doctorId
    );
}