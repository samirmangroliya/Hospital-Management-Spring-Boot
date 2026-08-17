package com.hospital.doctor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @GetMapping
    public Map<String, Object> getDoctors() {
        return Map.of(
                "service", "doctor-service",
                "doctors", new String[]{"Dr. Smith", "Dr. Patel"},
                "message", "Doctor service is working"
        );
    }
}