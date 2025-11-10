package com.tecnocampus.project.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String message) {
        Map<String, String> body = new HashMap<>();
        body.put("error", message);
        return new ResponseEntity<>(body, status);
    }


    @ExceptionHandler({
            com.tecnocampus.project.application.exceptions.LoyaltyTierNotFoundException.class,
            com.tecnocampus.project.application.exceptions.MedicationNotFoundException.class,
            com.tecnocampus.project.application.exceptions.BatchNotFoundException.class,
            com.tecnocampus.project.application.exceptions.AlertNotFoundException.class,
            com.tecnocampus.project.application.exceptions.VetNotFoundException.class,
            com.tecnocampus.project.application.exceptions.PromotionNotFoundException.class,
            com.tecnocampus.project.application.exceptions.VisitNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @ExceptionHandler({
            com.tecnocampus.project.application.exceptions.LoyaltyTierAlreadyExistsException.class,
            com.tecnocampus.project.application.exceptions.VetAvailabilityConflictException.class,
            com.tecnocampus.project.application.exceptions.MedicationOutOfStockException.class
    })
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }


    @ExceptionHandler({
            com.tecnocampus.project.application.exceptions.PromotionInvalidException.class,
            com.tecnocampus.project.application.exceptions.InvalidRequestException.class
    })
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    @ExceptionHandler({
            IllegalArgumentException.class,
            com.tecnocampus.project.application.exceptions.VetUnavailableException.class
    })
    public ResponseEntity<Map<String, String>> handleBadRequestExtra(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    @ExceptionHandler({
            com.tecnocampus.project.domain.exceptions.LoyaltyTierNotFoundException.class,
            com.tecnocampus.project.domain.exceptions.MedicationOutOfStockException.class,
            com.tecnocampus.project.domain.exceptions.PromotionExpiredException.class,
            com.tecnocampus.project.domain.exceptions.VetAvailabilityConflictException.class
    })
    public ResponseEntity<Map<String, String>> handleDomainExceptions(RuntimeException ex) {
        if (ex instanceof com.tecnocampus.project.domain.exceptions.LoyaltyTierNotFoundException) {
            return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        if (ex instanceof com.tecnocampus.project.domain.exceptions.VetAvailabilityConflictException
                || ex instanceof com.tecnocampus.project.domain.exceptions.MedicationOutOfStockException) {
            return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
        }
        if (ex instanceof com.tecnocampus.project.domain.exceptions.PromotionExpiredException) {
            return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error: " + ex.getMessage());
    }
}
