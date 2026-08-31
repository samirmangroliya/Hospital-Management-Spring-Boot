package com.hospital.appointment.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_NAME = "appointment-saga-topic";

    // Runs every 5 seconds to look for new events
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByProcessedFalse();

        for (OutboxEvent event : pendingEvents) {
            try {
                // Publish JSON payload to Kafka topic
                kafkaTemplate.send(TOPIC_NAME, event.getAggregateId(), event.getPayload()).get();

                // Mark event as processed so it isn't sent again
                event.setProcessed(true);
                outboxEventRepository.save(event);

                log.info("Successfully published outbox event ID: {} to Kafka topic: {}", event.getId(), TOPIC_NAME);
            } catch (Exception e) {
                log.error("Failed to publish outbox event ID: {} to Kafka", event.getId(), e);
                // Leave processed = false to retry on the next execution cycle
            }
        }
    }
}