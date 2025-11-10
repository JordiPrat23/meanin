package com.tecnocampus.project.domain.exceptions;

public class LoyaltyTierNotFoundException extends RuntimeException {
    public LoyaltyTierNotFoundException(String message) {
        super(message);
    }
}

