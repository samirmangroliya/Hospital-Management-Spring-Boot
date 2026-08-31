package com.hospital.appointment.service;

import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.AppointmentStatusRequest;

import java.util.List;

public interface AppointmentService {

        AppointmentResponse create(AppointmentRequest request);

        /*
         * AppointmentResponse getById(Long id);
         * 
         * List<AppointmentResponse> getAll();
         * 
         * AppointmentResponse update(
         * Long id,
         * AppointmentRequest request
         * );
         * 
         * AppointmentResponse updateStatus(
         * Long id,
         * AppointmentStatusRequest request
         * );
         * 
         * void delete(Long id);
         */
}