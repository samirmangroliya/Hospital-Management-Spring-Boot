package com.hospital.patient.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {

    public static <T> BaseResponse<T> success(
            String message,
            T data) {

        return new BaseResponse<>(
                true,
                message,
                data,
                Instant.now()
        );
    }

    public static <T> BaseResponse<T> failure(
            String message) {

        return new BaseResponse<>(
                false,
                message,
                null,
                Instant.now()
        );
    }
}