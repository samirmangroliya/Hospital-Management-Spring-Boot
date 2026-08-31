package com.hospital.appointment.controller;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.AppointmentStatusRequest;
import com.hospital.appointment.service.AppointmentService;
import com.hospital.common.response.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody AppointmentRequest request
    ) {

        AppointmentResponse response =
                appointmentService.create(request);

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
            @PathVariable
            @Positive(message = "Appointment ID must be positive")
            Long id
    ) {

        AppointmentResponse response =
                appointmentService.getById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment fetched successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAll() {

        List<AppointmentResponse> response =
                appointmentService.getAll();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointments fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentResponse>> update(
            @PathVariable
            @Positive(message = "Appointment ID must be positive")
            Long id,

            @Valid @RequestBody AppointmentRequest request
    ) {

        AppointmentResponse response =
                appointmentService.update(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment updated successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateStatus(
            @PathVariable
            @Positive(message = "Appointment ID must be positive")
            Long id,

            @Valid @RequestBody AppointmentStatusRequest request
    ) {

        AppointmentResponse response =
                appointmentService.updateStatus(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment status updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable
            @Positive(message = "Appointment ID must be positive")
            Long id
    ) {

        appointmentService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Appointment deleted successfully",
                        null
                )
        );
    }
}