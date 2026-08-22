package com.hospital.appointment.service;

import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.CreateAppointmentRequest;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.exception.AppointmentNotFoundException;
import com.hospital.appointment.repository.AppointmentRepository;

import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository
    ) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request
    ) {

        Appointment appointment = new Appointment();

        appointment.setPatientId(request.patientId());
        appointment.setDoctorId(request.doctorId());
        appointment.setAppointmentTime(
                request.appointmentTime()
        );
        appointment.setStatus("BOOKED");

        Appointment saved =
                appointmentRepository.save(appointment);

        return toResponse(saved);
    }

    @Override
    public AppointmentResponse getAppointment(
            Long id
    ) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(
                                () ->
                                        new AppointmentNotFoundException(id)
                        );

        return toResponse(appointment);
    }

    private AppointmentResponse toResponse(
            Appointment appointment
    ) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentTime(),
                appointment.getStatus()
        );
    }
}