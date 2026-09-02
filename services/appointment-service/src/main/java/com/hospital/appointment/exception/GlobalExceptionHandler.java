package com.hospital.appointment.exception;

import com.hospital.common.response.ApiResponse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }
     
    @ExceptionHandler(AppointmentException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppointmentConflict(
            AppointmentException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) 
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
        public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(
                        MethodNotAllowedException exception) {

                return ResponseEntity
                                .status(HttpStatus.METHOD_NOT_ALLOWED)
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


    @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Void>> handleGeneralException(
                        Exception exception) {

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.failure("An unexpected error occurred."+ exception.getMessage()));
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingOrInvalidBody(HttpMessageNotReadableException ex) {
        String message = "Required request body is missing or malformed.";

        // Unpack Jackson's nested cause to give a cleaner hint if it's a date/time parsing error
        Throwable cause = ex.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            message = "Invalid format provided for one of the fields. Check date-time values (e.g., valid hours 0-23 and minutes 0-59).";
        } else if (cause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException) {
            message = "Mismatched input type. Ensure the request body matches the expected structure.";
        } else if (cause instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) {
            message = "Unrecognized field in the request body. Please check for typos or unexpected fields.";
        } else {
            message = "Malformed JSON or missing required fields in the request body."+ex.getMessage();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(message));
    }
}