package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentTimeAndStatusNot(
            Long doctorId,
            LocalDateTime appointmentTime,
            AppointmentStatus status
    );

    boolean existsByPatientIdAndAppointmentTimeAndStatusNot(
            Long patientId,
            LocalDateTime appointmentTime,
            AppointmentStatus status
    );

    Optional<Appointment> findByIdAndStatusNot(
            Long id,
            AppointmentStatus status
    );
}