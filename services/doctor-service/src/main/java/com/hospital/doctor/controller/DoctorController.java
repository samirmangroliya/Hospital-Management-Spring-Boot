package com.hospital.doctor.controller;

import com.hospital.common.response.ApiResponse;
import com.hospital.doctor.dto.DoctorInternalResponse;
import com.hospital.doctor.dto.DoctorRequest;
import com.hospital.doctor.entity.Doctor;
import com.hospital.doctor.service.DoctorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@Validated
public class DoctorController {

        private final DoctorService doctorService;

        public DoctorController(DoctorService doctorService) {
                this.doctorService = doctorService;
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<Doctor>>> getAllDoctors() {

                List<Doctor> doctors = doctorService.getAllDoctors();

                if (doctors.isEmpty()) {
                        return ResponseEntity.ok(
                                        ApiResponse.failure("No doctors found"));
                }

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Doctors fetched successfully",
                                                doctors));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Doctor>> getDoctor(
                        @PathVariable @Positive(message = "Invalid doctor ID") Long id) {

                Doctor doctor = doctorService.getDoctorById(id);

                if (doctor == null) {
                        return ResponseEntity.ok(
                                        ApiResponse.failure("Doctor not found with id: " + id));
                }
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Doctor fetched successfully",
                                                doctor));
        }

        @GetMapping("/internal/{id}")
        public ResponseEntity<DoctorInternalResponse> getInternalDoctor(
                        @PathVariable @Positive(message = "Invalid doctor ID") Long id) {
                Doctor doctor = doctorService.getDoctorById(id);

                if (doctor == null) {
                        return ResponseEntity.notFound().build();
                }
                
                DoctorInternalResponse response = new DoctorInternalResponse(
                                doctor.getFirstName(),
                                doctor.getLastName(),
                                doctor.getEmail(),
                                doctor.getPhone(),
                                doctor.getSpecialization()
                );
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}/exists")
        public boolean isDoctorExists(
                        @PathVariable @Positive(message = "Invalid doctor ID") Long id) {

                return doctorService.isDoctorExists(id);
        }

        @PostMapping
        public ResponseEntity<ApiResponse<Doctor>> createDoctor(
                        @Valid @RequestBody DoctorRequest request) {

                Doctor doctor = doctorService.createDoctor(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                "Doctor created successfully",
                                                                doctor));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<Doctor>> updateDoctor(
                        @PathVariable @Positive(message = "Invalid doctor ID") Long id,
                        @Valid @RequestBody DoctorRequest request) {

                Doctor doctor = doctorService.updateDoctor(id, request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Doctor updated successfully",
                                                doctor));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteDoctor(
                        @PathVariable @Positive(message = "Invalid doctor ID") Long id) {

                doctorService.deleteDoctor(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Doctor deleted successfully",
                                                null));
        }
}