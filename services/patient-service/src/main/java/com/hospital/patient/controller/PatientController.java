package com.hospital.patient.controller;

import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.entity.Patient;
import com.hospital.patient.response.BaseResponse;
import com.hospital.patient.service.PatientService;

import jakarta.validation.Valid;

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
    public ResponseEntity<BaseResponse<Patient>> createPatient(
            @Valid @RequestBody PatientRequest request) {

        Patient patient = patientService.createPatient(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        BaseResponse.success(
                                "Patient created successfully",
                                patient));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<Patient>>> getAllPatients() {

        List<Patient> patients = patientService.getAllPatients();

        return ResponseEntity.ok(
                BaseResponse.success(
                        "Patients fetched successfully",
                        patients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Patient>> getPatient(
            @PathVariable Long id) {

        Patient patient = patientService.getPatientById(id);

        return ResponseEntity.ok(
                BaseResponse.success(
                        "Patient fetched successfully",
                        patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<Patient>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {

        Patient patient = patientService.updatePatient(id, request);

        return ResponseEntity.ok(
                BaseResponse.success(
                        "Patient updated successfully",
                        patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.ok(
                BaseResponse.success(
                        "Patient deleted successfully",
                        null));
    }
}