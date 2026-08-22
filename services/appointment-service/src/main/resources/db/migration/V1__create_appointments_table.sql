CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,

    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,

    appointment_time TIMESTAMP WITH TIME ZONE NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_appointments_patient_id
    ON appointments (patient_id);

CREATE INDEX idx_appointments_doctor_id
    ON appointments (doctor_id);

CREATE INDEX idx_appointments_doctor_time
    ON appointments (doctor_id, appointment_time);