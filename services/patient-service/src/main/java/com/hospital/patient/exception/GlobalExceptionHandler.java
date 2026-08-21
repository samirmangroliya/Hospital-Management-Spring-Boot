package com.hospital.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hospital.patient.response.BaseResponse;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(PatientAlreadyExistsException.class)
        public ResponseEntity<BaseResponse<Void>> handlePatientAlreadyExists(
                        PatientAlreadyExistsException exception) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(
                                                BaseResponse.failure(
                                                                exception.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<BaseResponse<Map<String, String>>> handleValidation(
                        MethodArgumentNotValidException exception) {

                Map<String, String> errors = new LinkedHashMap<>();

                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(
                                                new BaseResponse<>(
                                                                false,
                                                                "Validation failed",
                                                                errors,
                                                                Instant.now()));
        }

        @ExceptionHandler(PatientNotFoundException.class)
        public ResponseEntity<BaseResponse<Void>> handlePatientNotFound(
                        PatientNotFoundException exception) {

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(
                                                BaseResponse.failure(
                                                                exception.getMessage()));
        }
}