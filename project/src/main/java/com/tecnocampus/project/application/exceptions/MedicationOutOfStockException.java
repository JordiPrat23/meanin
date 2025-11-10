package com.tecnocampus.project.application.exceptions;

public class MedicationOutOfStockException extends RuntimeException {
    public MedicationOutOfStockException(String message) {
        super(message);
    }
}

