package com.hospital.patient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @GetMapping
    public Map<String, Object> getPatients() {
        return Map.of(
                "service", "patient-service",
                "patients", List.of("John Doe", "Jane Doe"),
                "message", "Patient service is working"
        );
    }
}