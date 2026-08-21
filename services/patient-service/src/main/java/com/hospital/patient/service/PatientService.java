package com.hospital.patient.service;

import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.entity.Patient;
import com.hospital.patient.exception.PatientAlreadyExistsException;
import com.hospital.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import com.hospital.patient.exception.PatientNotFoundException;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(PatientRequest request) {

        if (patientRepository.existsByEmail(request.email())) {
            throw new PatientAlreadyExistsException(
                    "Patient already exists with email: " + request.email()
            );
        }

        Patient patient = new Patient();

        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setEmail(request.email());
        patient.setPhone(request.phone());

        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient not found with id: " + id)
                );
    }

     public Patient updatePatient(Long id, PatientRequest request) {

        Patient patient = patientRepository.findById(id)
            .orElseThrow(() ->
                    new PatientNotFoundException(
                            "Patient not found with id: " + id
                    ));

        if (!patient.getEmail().equals(request.email())
            && patientRepository.existsByEmail(request.email())) {

        throw new PatientAlreadyExistsException(
                "Patient already exists with email: " + request.email()
        );
    }

    patient.setFirstName(request.firstName());
    patient.setLastName(request.lastName());
    patient.setEmail(request.email());
    patient.setPhone(request.phone());

    return patientRepository.save(patient);
}

public void deletePatient(Long id) {

    Patient patient = patientRepository.findById(id)
            .orElseThrow(() ->
                    new PatientNotFoundException(
                            "Patient not found with id: " + id
                    ));

    patientRepository.delete(patient);
}

    
}