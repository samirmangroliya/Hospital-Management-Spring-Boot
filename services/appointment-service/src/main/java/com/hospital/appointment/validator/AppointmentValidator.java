package com.hospital.appointment.validator;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.exception.AppointmentException;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentValidator {

    public void validate(AppointmentRequest request) {
        if (request == null) {
            throw new AppointmentException("Appointment request cannot be null.");
        }

        Long patientId = request.patientId();
        Long doctorId = request.doctorId();
        LocalDateTime appointmentTime = request.appointmentTime();

        if (patientId == null || patientId == 0) {
            throw new AppointmentException("Patient ID is invalid.");
        } else if (doctorId == null || doctorId == 0) {
            throw new AppointmentException("Doctor ID is invalid.");
        } else if (appointmentTime == null || appointmentTime.isBefore(LocalDateTime.now())) {
            throw new AppointmentException("Appointment time must be in the future.");
        } else if (appointmentTime.getDayOfWeek().getValue() > 5) {
            throw new AppointmentException(
                    "Appointment day should be in Monday to Friday.");
        } else {
            // Check working hours (e.g., 9 AM to 5 PM)
            int hour = appointmentTime.getHour();
            if (hour < 9 || hour >= 17) {
                throw new AppointmentException(
                        "Appointment time is not within working hours. Time is 9 AM to 5 PM.");
            }

            // Check if minutes are strictly on the hour or half-hour (0 or 30)
            int minute = appointmentTime.getMinute();
            if (minute != 0 && minute != 30) {
                throw new AppointmentException(
                        "Appointment time is not on the hour or half-hour.");
            }
        }

    }
}