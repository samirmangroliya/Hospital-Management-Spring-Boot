package com.hospital.patient.repository;

import com.hospital.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findByEmail(String email);

    void updatePatientNameByEmail(String name, String email);

    void deleteByEmail(String email);

    void createOrUpdatePatient(Patient patient);
}