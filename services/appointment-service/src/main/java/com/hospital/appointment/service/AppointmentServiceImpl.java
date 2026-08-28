package com.hospital.appointment.service;

import com.hospital.appointment.domain.gateway.DoctorGateway;
import com.hospital.appointment.domain.gateway.PatientGateway;
import com.hospital.appointment.dto.AppointmentRequest;
import com.hospital.appointment.dto.AppointmentResponse;
import com.hospital.appointment.dto.AppointmentStatusRequest;
import com.hospital.appointment.entity.Appointment;
import com.hospital.appointment.entity.AppointmentStatus;
import com.hospital.appointment.exception.AppointmentException;
import com.hospital.appointment.exception.InvalidAppointmentStateException;
import com.hospital.appointment.exception.ResourceNotFoundException;
import com.hospital.appointment.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

        private final AppointmentRepository appointmentRepository;
        private final DoctorGateway doctorGateway;
        private final PatientGateway patientGateway;

        @Override
        @Transactional
        public AppointmentResponse create(AppointmentRequest request) {

                // 1. Verify Patient exists
                boolean patientExists = patientGateway.checkPatientExists(request.patientId());
                if (!patientExists) {
                        throw new ResourceNotFoundException("Patient with ID " + request.patientId() + " not found.");
                }

                // 2. Verify Doctor exists
                boolean doctorExists = doctorGateway.checkDoctorExists(request.doctorId());
                if (!doctorExists) {
                        throw new ResourceNotFoundException("Doctor with ID " + request.doctorId() + " not found.");
                }

                validateAppointmentSlot(request.appointmentTime());

                // Check Availability of Doctor and Patient for the given appointment time
                validateDoctorAvailability(
                                request.doctorId(),
                                request.appointmentTime(),
                                null);

                validatePatientAvailability(
                                request.patientId(),
                                request.appointmentTime(),
                                null);

                // 3. Create Appointment
                Appointment appointment = Appointment.builder()
                                .patientId(request.patientId())
                                .doctorId(request.doctorId())
                                .appointmentTime(request.appointmentTime())
                                .status(AppointmentStatus.SCHEDULED)
                                .build();

                Appointment saved = appointmentRepository.save(appointment);

                return toResponse(saved);
        }

        @Override
        public AppointmentResponse getById(Long id) {

                Appointment appointment = appointmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Appointment not found with id: " + id));

                return toResponse(appointment);
        }

        @Override
        public List<AppointmentResponse> getAll() {

                return appointmentRepository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
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

                if (current == AppointmentStatus.SCHEDULED
                                && next == AppointmentStatus.COMPLETED) {

                        throw new InvalidAppointmentStateException(
                                        "Scheduled appointment must be confirmed before completion");
                }

                if (current == AppointmentStatus.SCHEDULED
                                && next == AppointmentStatus.CANCELLED) {
                        return;
                }

                if (current == AppointmentStatus.SCHEDULED
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
                                appointment.getAppointmentTime(),
                                appointment.getStatus(),
                                appointment.getCreatedAt(),
                                appointment.getUpdatedAt());
        }

        public void validateAppointmentSlot(LocalDateTime appointmentTime) {
                // 1. Check if it's in the past
                if (appointmentTime.isBefore(LocalDateTime.now())) {
                        throw new AppointmentException(
                                "Appointment time invalid, appointment time should be in future.");
                }

                // 2. Check if it's a weekday (Monday = 1, Sunday = 7)
                if (appointmentTime.getDayOfWeek().getValue() > 5) {
                          throw new AppointmentException(
                                "Appointment day should be in Monday to Friday.");
                }

                // 3. Check working hours (e.g., 9 AM to 5 PM)
                int hour = appointmentTime.getHour();
                if (hour < 9 || hour >= 17) {
                         throw new AppointmentException(
                                "Appointment time is not within working hours. Time is 9 AM to 5 PM.");
                }

                // 4. Check if minutes are strictly on the hour or half-hour (0 or 30)
                int minute = appointmentTime.getMinute();
                if (minute != 0 && minute != 30) {
                          throw new AppointmentException(
                                "Appointment time is not on the hour or half-hour.");
                }
        }
}