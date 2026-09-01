package com.hospital.doctor.service;

import com.hospital.doctor.dto.DoctorRequest;
import com.hospital.doctor.entity.Doctor;
import com.hospital.doctor.exception.DoctorAlreadyExistsException;
import com.hospital.doctor.exception.DoctorNotFoundException;
import com.hospital.doctor.repository.DoctorRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
 
    public Doctor getDoctorById(Long id) {

        return doctorRepository.findById(id)
                .orElse(null);
    }

    public Doctor createDoctor(DoctorRequest request) {

        if (doctorRepository.existsByEmail(request.email())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor already exists with email: " + request.email()
            );
        }

        if (doctorRepository.existsByPhone(request.phone())) {
            throw new DoctorAlreadyExistsException(
                    "Doctor already exists with phone: " + request.phone()
            );
        }

        Doctor doctor = new Doctor();

        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setSpecialization(request.specialization());
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());

        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, DoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        if (!doctor.getEmail().equals(request.email())
                && doctorRepository.existsByEmail(request.email())) {

            throw new DoctorAlreadyExistsException(
                    "Doctor already exists with email: "
                            + request.email()
            );
        }

        doctor.setFirstName(request.firstName());
        doctor.setLastName(request.lastName());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setSpecialization(request.specialization());

        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        doctorRepository.delete(doctor);
    }

    public boolean isDoctorExists(Long id) {
       Doctor doctor = doctorRepository.findById(id)
                .orElse(null);

        return doctor != null;       
    }
}