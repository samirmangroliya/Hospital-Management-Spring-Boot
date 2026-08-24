package com.hospital.patient.controller;

import com.hospital.common.response.ApiResponse;
import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.entity.Patient;
import com.hospital.patient.service.PatientService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

        private final PatientService patientService;

        public PatientController(PatientService patientService) {
                this.patientService = patientService;
        }

        @PostMapping
        public ResponseEntity<ApiResponse<Patient>> createPatient(
                        @Valid @RequestBody PatientRequest request) {

                Patient patient = patientService.createPatient(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                "Patient created successfully",
                                                                patient));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<Patient>>> getAllPatients() {

                List<Patient> patients = patientService.getAllPatients();

                if (patients.isEmpty()) {
                        return ResponseEntity.ok(
                                        ApiResponse.failure(
                                                        "No patients found"));
                }
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Patients fetched successfully",
                                                patients));
        }

        // ---------------------------------------------------------
        // GET BY ID
        // ---------------------------------------------------------
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Patient>> getPatient(
                        @PathVariable @Positive(message = "Invalid patient ID") Long id) {

                Patient patient = patientService.getPatientById(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Patient fetched successfully",
                                                patient));
        }

        // ---------------------------------------------------------
        // Patient EXISTS
        // ---------------------------------------------------------
        @GetMapping("/{id}/exists")
        public boolean isPatientExists(
                        @PathVariable @Positive(message = "Invalid patient ID") Long id) {
                return patientService.isPatientExists(id);
        }

        // ---------------------------------------------------------
        // UPDATE
        // ---------------------------------------------------------
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<Patient>> updatePatient(
                        @PathVariable @Positive(message = "Invalid patient ID") Long id,

                        @Valid @RequestBody PatientRequest request) {

                Patient patient = patientService.updatePatient(id, request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Patient updated successfully",
                                                patient));
        }

        // ---------------------------------------------------------
        // DELETE
        // ---------------------------------------------------------
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deletePatient(
                        @PathVariable @Positive(message = "Invalid patient ID") Long id) {

                patientService.deletePatient(id);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Patient deleted successfully",
                                                null));
        }
}