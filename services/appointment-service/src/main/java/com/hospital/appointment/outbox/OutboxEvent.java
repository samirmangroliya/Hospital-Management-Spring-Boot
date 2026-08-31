package com.hospital.appointment.outbox;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType; // e.g., "APPOINTMENT_SAGA"
    private String aggregateId;   // e.g., Appointment ID as String
    private String eventType;     // e.g., "APPOINTMENT_INITIATED"

    @Column(columnDefinition = "TEXT")
    private String payload;       // JSON string of the SagaEventPayload

    private boolean processed = false;

    private Instant createdAt = Instant.now();
}