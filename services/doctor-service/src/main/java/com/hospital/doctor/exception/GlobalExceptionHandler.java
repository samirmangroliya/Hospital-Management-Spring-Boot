package com.hospital.doctor.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hospital.common.response.ApiResponse;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(DoctorNotFoundException.class)
        public ResponseEntity<ApiResponse<Void>> handleDoctorNotFound(
                        DoctorNotFoundException exception) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.failure(exception.getMessage()));
        }

        @ExceptionHandler(DoctorAlreadyExistsException.class)
        public ResponseEntity<ApiResponse<Void>> handleDoctorAlreadyExists(
                        DoctorAlreadyExistsException exception) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ApiResponse.failure(exception.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
                        MethodArgumentNotValidException exception) {

                Map<String, String> errors = new LinkedHashMap<>();

                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                String detailedMessage = errors.entrySet().stream()
                                .map(entry -> entry.getValue())
                                .collect(Collectors.joining(", "));

                String finalMessage = "Validation failed: " + detailedMessage;

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.failure(finalMessage));
        }
}