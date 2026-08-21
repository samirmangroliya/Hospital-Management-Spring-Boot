package com.hospital.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/appointments")
    public ResponseEntity<Map<String, Object>> appointmentFallback() {

        Map<String, Object> response = new HashMap<>();

        response.put("service", "appointment-service");
        response.put("status", "DEGRADED");
        response.put("message",
                "Appointment service is temporarily unavailable");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}
