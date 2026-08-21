package com.hospital.appointment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger log =
            LoggerFactory.getLogger(AppointmentController.class);

    private final Tracer tracer;

    public AppointmentController(Tracer tracer) {
        this.tracer = tracer;
    }
    
    @GetMapping
    public Map<String, Object> getAppointments(@Value("${server.port}") String port) {

        Span span = tracer.currentSpan();

        log.info(
                "Fetching appointments. traceId={}, spanId={}, port={}",
                span != null ? span.context().traceId() : "NO-SPAN",
                span != null ? span.context().spanId() : "NO-SPAN",
                port
        );

       return Map.of(
                "appointments", List.of(
                        "Appointment 1",
                        "Appointment 2",
                        "Appointment 3"
                ),
                "service", "appointment-service",
                "message", "Appointment service is working",
                "port", port
        );
    }
}