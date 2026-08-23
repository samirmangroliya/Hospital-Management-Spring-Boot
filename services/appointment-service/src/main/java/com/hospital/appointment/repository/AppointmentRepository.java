package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.entity.AppointmentStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    Page<Appointment> findByDoctorId(
            Long doctorId,
            Pageable pageable
    );

    Page<Appointment> findByPatientId(
            Long patientId,
            Pageable pageable
    );

    Page<Appointment> findByStatus(
            AppointmentStatus status,
            Pageable pageable
    );
}