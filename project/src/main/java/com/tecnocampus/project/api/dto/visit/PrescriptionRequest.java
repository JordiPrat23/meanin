package com.tecnocampus.project.api.dto.visit;

public record PrescriptionRequest(
        Long medicationId,
        int quantity,
        String dosageInstructions,
        String duration
) {}

