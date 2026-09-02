package com.hospital.appointment.service;

import com.hospital.appointment.domain.gateway.DoctorGateway;
import com.hospital.appointment.domain.gateway.PatientGateway;
import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.AppointmentStatusRequest;
import com.hospital.appointment.dto.DoctorInfo;
import com.hospital.appointment.dto.PatientInfo;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.entity.AppointmentStatus;
import com.hospital.appointment.exception.AppointmentException;
import com.hospital.appointment.exception.InvalidAppointmentStateException;
import com.hospital.appointment.exception.ResourceNotFoundException;
import com.hospital.appointment.outbox.OutboxEventService;
import com.hospital.appointment.repository.AppointmentRepository;
import com.hospital.appointment.saga.SagaEventPayload;
import com.hospital.appointment.validator.AppointmentValidator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final OutboxEventService outboxEventService;
        private final AppointmentValidator appointmentValidator;
        private final DoctorGateway doctorGateway;
        private final PatientGateway patientGateway;

        @Override
        @Transactional
        public AppointmentResponse create(AppointmentRequest request) {
                // 1. Validate the request first using our separate class
                appointmentValidator.validate(request);

                if (patientGateway.getPatientById(request.patientId()) == null) {
                        throw new AppointmentException("Patient not found with ID: " + request.patientId());
                }

                if (doctorGateway.getDoctorById(request.doctorId()) == null) {
                        throw new AppointmentException("Doctor not found with ID: " + request.doctorId());
                }

                validateDoctorAvailability(
                                request.doctorId(),
                                request.appointmentTime(),
                                null);

                validatePatientAvailability(
                                request.patientId(),
                                request.appointmentTime(),
                                null);

                // 2. Save appointment with PENDING status
                Appointment appointment = Appointment.builder()
                                .patientId(request.patientId())
                                .doctorId(request.doctorId())
                                .appointmentTime(request.appointmentTime())
                                .status(AppointmentStatus.INITIATED)
                                .build();

                Appointment savedAppointment = appointmentRepository.save(appointment);

                // 3. Create the Saga Payload object
                SagaEventPayload eventPayload = new SagaEventPayload(
                                savedAppointment.getId(),
                                savedAppointment.getPatientId(),
                                savedAppointment.getDoctorId(),
                                savedAppointment.getAppointmentTime(),
                                "APPOINTMENT_INITIATED");

                // 4. Save to Outbox atomically in the same transaction
                outboxEventService.saveEvent(
                                "APPOINTMENT_SAGA",
                                savedAppointment.getId().toString(),
                                "APPOINTMENT_INITIATED",
                                eventPayload);

                return toResponse(savedAppointment);
        }

        @Override
        public AppointmentResponse getById(Long id) {

                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Appointment not found with id: " + id));
                DoctorInfo doctor = doctorGateway.getDoctorById(appointment.getDoctorId());
                ;
                PatientInfo patient = patientGateway.getPatientById(appointment.getPatientId());
                return new AppointmentResponse(
                                appointment.getId(),
                                appointment.getPatientId(),
                                appointment.getDoctorId(),
                                patient,
                                doctor,
                                appointment.getAppointmentTime(),
                                appointment.getStatus(),
                                appointment.getCreatedAt(),
                                appointment.getUpdatedAt());
        }

        // @Override
        // public List<AppointmentResponse> getAll() {

        // return appointmentRepository.findAll()
        // .stream()
        // .map(this::toResponse)
        // .toList();
        // }

        @Override
        public List<AppointmentResponse> getAll() {
                List<Appointment> appointments = appointmentRepository.findAll();

                return appointments.stream().map(appointment -> {
                        DoctorInfo doctor = doctorGateway.getDoctorById(appointment.getDoctorId());
                        ;
                        PatientInfo patient = patientGateway.getPatientById(appointment.getPatientId());
                        return new AppointmentResponse(
                                        appointment.getId(),
                                        appointment.getPatientId(),
                                        appointment.getDoctorId(),
                                        patient,
                                        doctor,
                                        appointment.getAppointmentTime(),
                                        appointment.getStatus(),
                                        appointment.getCreatedAt(),
                                        appointment.getUpdatedAt());
                }).toList();
        }

        @Override
        @Transactional
        public AppointmentResponse update(
                        Long id,
                        AppointmentRequest request) {

                Appointment appointment = getAppointment(id);

                validateCanModify(appointment);

                validateDoctorAvailability(
                                request.doctorId(),
                                request.appointmentTime(),
                                id);

                validatePatientAvailability(
                                request.patientId(),
                                request.appointmentTime(),
                                id);

                appointment.update(
                                request.patientId(),
                                request.doctorId(),
                                request.appointmentTime());

                return toResponse(appointment);
        }

        @Override
        @Transactional
        public AppointmentResponse updateStatus(
                        Long id,
                        AppointmentStatusRequest request) {

                Appointment appointment = getAppointment(id);

                validateStatusTransition(
                                appointment.getStatus(),
                                request.status());

                appointment.updateStatus(request.status());

                return toResponse(appointment);
        }

        @Override
        @Transactional
        public void delete(Long id) {

                Appointment appointment = getAppointment(id);

                validateCanModify(appointment);

                appointmentRepository.delete(appointment);
        }

        private Appointment getAppointment(Long id) {

                return appointmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Appointment not found with id: " + id));
        }

        private void validateDoctorAvailability(
                        Long doctorId,
                        java.time.LocalDateTime appointmentTime,
                        Long appointmentId) {

                boolean exists = appointmentRepository
                                .existsByDoctorIdAndAppointmentTimeAndStatusNot(
                                                doctorId,
                                                appointmentTime,
                                                AppointmentStatus.CANCELLED);
                if (!exists) {
                        return;
                }

                if (appointmentId != null) {
                        Appointment existing = appointmentRepository
                                        .findByIdAndStatusNot(
                                                        appointmentId,
                                                        AppointmentStatus.CANCELLED)
                                        .orElse(null);

                        if (existing != null
                                        && existing.getDoctorId().equals(doctorId)
                                        && existing.getAppointmentTime().equals(appointmentTime)) {
                                return;
                        }
                }

                throw new AppointmentException(
                                "Doctor already has an appointment at this time");
        }

        private void validatePatientAvailability(
                        Long patientId,
                        java.time.LocalDateTime appointmentTime,
                        Long appointmentId) {

                boolean exists = appointmentRepository
                                .existsByPatientIdAndAppointmentTimeAndStatusNot(
                                                patientId,
                                                appointmentTime,
                                                AppointmentStatus.CANCELLED);

                if (!exists) {
                        return;
                }

                if (appointmentId != null) {
                        Appointment existing = appointmentRepository
                                        .findByIdAndStatusNot(
                                                        appointmentId,
                                                        AppointmentStatus.CANCELLED)
                                        .orElse(null);

                        if (existing != null
                                        && existing.getPatientId().equals(patientId)
                                        && existing.getAppointmentTime().equals(appointmentTime)) {
                                return;
                        }
                }

                throw new AppointmentException(
                                "Patient already has an appointment at this time");
        }

        private void validateCanModify(Appointment appointment) {

                if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
                        throw new InvalidAppointmentStateException(
                                        "Completed appointment cannot be modified");
                }

                if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
                        throw new InvalidAppointmentStateException(
                                        "Cancelled appointment cannot be modified");
                }
        }

        private void validateStatusTransition(
                        AppointmentStatus current,
                        AppointmentStatus next) {

                if (current == AppointmentStatus.CANCELLED) {
                        throw new InvalidAppointmentStateException(
                                        "Cancelled appointment cannot change status");
                }

                if (current == AppointmentStatus.COMPLETED) {
                        throw new InvalidAppointmentStateException(
                                        "Completed appointment cannot change status");
                }

                if (current == AppointmentStatus.INITIATED
                                && next == AppointmentStatus.COMPLETED) {

                        throw new InvalidAppointmentStateException(
                                        "Scheduled appointment must be confirmed before completion");
                }

                if (current == AppointmentStatus.INITIATED
                                && next == AppointmentStatus.CANCELLED) {
                        return;
                }

                if (current == AppointmentStatus.INITIATED
                                && next == AppointmentStatus.CONFIRMED) {
                        return;
                }

                if (current == AppointmentStatus.CONFIRMED
                                && next == AppointmentStatus.COMPLETED) {
                        return;
                }

                if (current == AppointmentStatus.CONFIRMED
                                && next == AppointmentStatus.CANCELLED) {
                        return;
                }

                if (current == next) {
                        return;
                }

                throw new InvalidAppointmentStateException(
                                "Invalid appointment status transition from "
                                                + current + " to " + next);
        }

        private AppointmentResponse toResponse(Appointment appointment) {

                return new AppointmentResponse(
                                appointment.getId(),
                                appointment.getPatientId(),
                                appointment.getDoctorId(),
                                null,
                                null,
                                appointment.getAppointmentTime(),
                                appointment.getStatus(),
                                appointment.getCreatedAt(),
                                appointment.getUpdatedAt());
        }
}