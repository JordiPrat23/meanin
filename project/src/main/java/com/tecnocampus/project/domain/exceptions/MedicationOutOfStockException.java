package com.tecnocampus.project.domain.exceptions;

public class MedicationOutOfStockException extends RuntimeException {
    public MedicationOutOfStockException(String message) {
        super(message);
    }
}

