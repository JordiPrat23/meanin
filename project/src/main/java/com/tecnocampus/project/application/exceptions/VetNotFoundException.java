package com.tecnocampus.project.application.exceptions;

public class VetNotFoundException extends RuntimeException {
    public VetNotFoundException(String message) {
        super(message);
    }
}

