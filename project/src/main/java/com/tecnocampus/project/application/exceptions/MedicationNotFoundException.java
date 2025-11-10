package com.tecnocampus.project.application.exceptions;

public class MedicationNotFoundException extends RuntimeException {
    public MedicationNotFoundException(String message) { super(message); }
}

