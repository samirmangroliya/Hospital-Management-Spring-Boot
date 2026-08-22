package com.hospital.appointment.controller;

import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.CreateAppointmentRequest;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.common.response.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {

        AppointmentResponse response =
                appointmentService.createAppointment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Appointment created successfully",
                                response
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getById(
            @PathVariable Long id
    ) {

        AppointmentResponse response =
                appointmentService.getAppointment(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment fetched successfully",
                        response
                )
        );
    }
}