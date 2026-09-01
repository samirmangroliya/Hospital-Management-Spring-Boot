CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,

    patient_id BIGINT NOT NULL,

    doctor_id BIGINT NOT NULL,

    appointment_time TIMESTAMP NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT chk_appointment_status CHECK (status IN ('INITIATED', 'PATIENT_VERIFIED', 'DOCTOR_VERIFIED', 'FAILED', 'CONFIRMED', 'COMPLETED', 'CANCELLED'))    
);

CREATE INDEX idx_appointment_doctor_time
    ON appointments (doctor_id, appointment_time);

CREATE INDEX idx_appointment_patient_time
    ON appointments (patient_id, appointment_time);

CREATE INDEX idx_appointment_status
    ON appointments (status);