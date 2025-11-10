package com.tecnocampus.project.domain.exceptions;

public class PromotionExpiredException extends RuntimeException {
    public PromotionExpiredException(String message) {
        super(message);
    }
}

