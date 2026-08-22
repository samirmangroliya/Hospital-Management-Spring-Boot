CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,

    date_of_birth DATE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_patients_email
    ON patients (email);

CREATE INDEX idx_patients_phone
    ON patients (phone);