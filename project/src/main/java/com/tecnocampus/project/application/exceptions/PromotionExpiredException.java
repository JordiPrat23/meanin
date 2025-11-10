package com.tecnocampus.project.application.exceptions;

public class PromotionExpiredException extends RuntimeException {
    public PromotionExpiredException(String message) {
        super(message);
    }
}

