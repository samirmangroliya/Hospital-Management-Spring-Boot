package com.hospital.appointment.service;

import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.CreateAppointmentRequest;

public interface AppointmentService {

    AppointmentResponse createAppointment(
            CreateAppointmentRequest request
    );

    AppointmentResponse getAppointment(
            Long id
    );
}