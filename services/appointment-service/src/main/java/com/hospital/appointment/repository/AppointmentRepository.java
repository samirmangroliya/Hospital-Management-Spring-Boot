package com.hospital.appointment.repository;

import com.hospital.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
 
}