package com.hospital.appointment.saga;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SagaCoordinationManager {

    private final Map<Long, CompletableFuture<String>> futures = new ConcurrentHashMap<>();

    public CompletableFuture<String> register(Long appointmentId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        futures.put(appointmentId, future);
        return future;
    }

    public void complete(Long appointmentId, String finalStatus) {
        CompletableFuture<String> future = futures.remove(appointmentId);
        if (future != null) {
            future.complete(finalStatus);
        }
    }
}