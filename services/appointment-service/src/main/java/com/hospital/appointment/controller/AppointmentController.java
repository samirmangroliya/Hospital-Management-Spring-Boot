package com.hospital.appointment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @GetMapping
    public Map<String, Object> getAppointments(@Value("${server.port}") String port) {
        return Map.of(
                "service", "appointment-service",
                "appointments", new String[]{"Appointment 1", "Appointment 2", "Appointment 3"},
                "message", "Appointment service is working",
                "port", port
        );
    }
}